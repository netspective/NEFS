/*****************************************************************************
 * Netspective Enterprise Frameworks
 * Windows Scripting Host (WSH) Application Build Launcher
 *****************************************************************************/

function addFilesToClassPath(classPath, folder)
{
	if(folder == null)
		return classPath;
		
	if(typeof(folder) == 'string')
	{
		if(fso.folderExists(folder))
			folder = fso.getFolder(folder);
		else
			return classPath;
	}

    var result = classPath;
    var enumerator = new Enumerator(folder.files);
    for (; !enumerator.atEnd(); enumerator.moveNext())
    {   
       var re = /\.jar$/
       var file = enumerator.item();
       var matches = re.exec(file.name);
       if(matches == null || matches.count == 0)
       	   continue;
    
       if(result.length > 0) result += PATH_DELIM;
       result += enumerator.item();
    }
    return result;
}

function addPathToClassPath(classPath, path)
{
	if(fso.fileExists(path) || fso.folderExists(path))
		return classPath + PATH_DELIM + path;
	else
		return classPath;
}

var PATH_DELIM = ";"
var shell = WScript.CreateObject("WScript.Shell");
var env   = shell.Environment;
var fso   = new ActiveXObject("Scripting.FileSystemObject");
var args  = WScript.arguments;

if(args.length < 2)
{
    WScript.echo("Application root directory required as first parameter.");
    WScript.echo("Library style 'ite', 'sde-ide', or 'sde-ant' expected as second parameter.");
	WScript.quit();
}

var appRootFolder = fso.getFolder(args(0));
var webInfFolder = fso.getFolder(appRootFolder + "\\WEB-INF");
var webInfLibFolderName = webInfFolder + "\\lib";
var libraryStyle = args(1);

if(libraryStyle != 'ite' && libraryStyle != 'sde-ide' && libraryStyle != 'sde-ant')
{
    WScript.echo("Library style 'ite', 'sde-ide', or 'sde-ant' expected as second parameter.");
	WScript.quit();
}

var projectClassPath = "";
projectClassPath = addPathToClassPath(projectClassPath, webInfFolder + "\\classes");

if(libraryStyle == 'ite')
{
	projectClassPath = addFilesToClassPath(projectClassPath, webInfLibFolderName);
}
else 
{
	var nefsHomeFolderName = env("NEFS_HOME");

	// set the default NEFS_HOME if one is not found in the environment
	if(nefsHomeFolderName == "")
		nefsHomeFolderName = "C:\\Projects\\Frameworks";

	if(! fso.folderExists(nefsHomeFolderName))
	{
		WScript.echo("NEFS_HOME folder " + nefsHomeFolderName + " does not exist.");
		WScript.quit();
	}
	var nefsHomeFolder = fso.getFolder(nefsHomeFolderName);

	var frameworks = new Enumerator(nefsHomeFolder.subFolders);
	for ( ; !frameworks.atEnd(); frameworks.moveNext())
	{
		var frameworkFolder = frameworks.item();

        // all the 3rd party redistributable library files
		var frameworkLibRedistFolderName = frameworkFolder + "\\lib\\redist";
		if(fso.folderExists(frameworkLibRedistFolderName))
   	    	projectClassPath = addFilesToClassPath(projectClassPath, frameworkLibRedistFolderName);

        // these are added because of com/netspective/<lib>/conf/*.xml resource files are here
   	    projectClassPath = addPathToClassPath(projectClassPath, frameworkFolder + "\\src\\java\\main");
	}

	if(libraryStyle == 'sde-ide')
	{
		// add classes that may be in use while compiling using an IDE
		projectClassPath = addPathToClassPath(projectClassPath, nefsHomeFolderName + "\\tools\\idea\\classes");
		projectClassPath = addPathToClassPath(projectClassPath, nefsHomeFolderName + "\\tools\\eclipse\\classes");
	}
	else
	{
		frameworks = new Enumerator(nefsHomeFolder.subFolders);
		for ( ; !frameworks.atEnd(); frameworks.moveNext())
		{
			var frameworkFolder = frameworks.item();
			projectClassPath = addPathToClassPath(projectClassPath, frameworkFolder + "/lib/netspective-"+ frameworkFolder.name.toLowerCase() +".jar");
		}
	}
}

var cmdLine = "cmd.exe /c java -classpath " + projectClassPath + " -Dapp.home.path="+ appRootFolder +" org.apache.tools.ant.Main ";
if(args.length > 2)
{
	for (i = 2; i < args.length; i++)
	   cmdLine += " " + args(i);
}

//WScript.echo(cmdLine);
var exec = shell.exec(cmdLine);

while(!exec.stdErr.AtEndOfStream)
	WScript.stdErr.WriteLine(exec.stdErr.ReadLine())

while(!exec.StdOut.AtEndOfStream)
	WScript.StdOut.WriteLine(exec.StdOut.ReadLine())


