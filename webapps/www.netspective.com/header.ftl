<html><head>

<title>${vc.navigationContext.getPageTitle()}</title>

<link rel="SHORTCUT ICON" href="${theme.getResourceUrl('/images/favicon.ico')}">
<link rel="stylesheet" href="${theme.getResourceUrl('/css/panel-input.css')}" type="text/css">
<link rel="stylesheet" href="${theme.getResourceUrl('/css/panel-output.css')}" type="text/css">
<link rel="stylesheet" href="${theme.getResourceUrl('/css/panel-content-dialog.css')}" type="text/css">
<link rel="stylesheet" href="${theme.getResourceUrl('/css/panel-content-report.css')}" type="text/css">
<link rel="stylesheet" href="${theme.getResourceUrl('/css/panel-content-text.css')}" type="text/css">

<script src="${theme.getResourceUrl('/scripts/panel.js')}" language="JavaScript1.1"></script>
<script src="${theme.getResourceUrl('/scripts/dialog.js')}" language="JavaScript1.2"></script>

<meta content="text/html; charset=iso-8859-1" http-equiv="Content-Type">
<meta content="j2ee pattern,j2ee application server,j2ee architecture,java application developer,xml rpc,xml programmer,xml basics,xml parsers,xp programming,eai software,eai architecture,uddi what" name="keywords">
<link type="text/css" href="${resourcesPath}/main.css" rel="stylesheet">
<csscriptdict></csscriptdict>
<script type="text/javascript"><!--

function newImage(arg) {
	if (document.images) {
		rslt = new Image();
		rslt.src = arg;
		return rslt;
	}
}
userAgent = window.navigator.userAgent;
browserVers = parseInt(userAgent.charAt(userAgent.indexOf("/")+1),10);
mustInitImg = true;
function initImgID() {di = document.images; if (mustInitImg && di) { for (var i=0; i<di.length; i++) { if (!di[i].id) di[i].id=di[i].name; } mustInitImg = false;}}
function findElement(n,ly) {
	d = document;
	if (browserVers < 4)		return d[n];
	if ((browserVers >= 6) && (d.getElementById)) {initImgID; return(d.getElementById(n))};
	var cd = ly ? ly.document : d;
	var elem = cd[n];
	if (!elem) {
		for (var i=0;i<cd.layers.length;i++) {
			elem = findElement(n,cd.layers[i]);
			if (elem) return elem;
		}
	}
	return elem;
}
function changeImages() {
	d = document;
	if (d.images) {
		var img;
		for (var i=0; i<changeImages.arguments.length; i+=2) {
			img = null;
			if (d.layers) {img = findElement(changeImages.arguments[i],0);}
			else {img = d.images[changeImages.arguments[i]];}
			if (img) {img.src = changeImages.arguments[i+1];}
		}
	}
}

// --></script>

		<csactiondict></csactiondict>
			<script type="text/javascript"><!--
var preloadFlag = false;
function preloadImages() {
	if (document.images) {
		over_home_6 = newImage(/*URL*/'${resourcesPath}/images/header/developers-over.gif');
		over_home_7 = newImage(/*URL*/'${resourcesPath}/images/header/products-over.gif');
		over_home_8 = newImage(/*URL*/'${resourcesPath}/images/header/downloads-over.gif');
		over_home_9 = newImage(/*URL*/'${resourcesPath}/images/header/buy-over.gif');
		over_home_13 = newImage(/*URL*/'${resourcesPath}/images/header/services-over.gif');
		over_home_14 = newImage(/*URL*/'${resourcesPath}/images/header/support-over.gif');
		over_home_15 = newImage(/*URL*/'${resourcesPath}/images/header/company-over.gif');
		over_home_21 = newImage(/*URL*/'${resourcesPath}/images/header/presentation-over.gif');
		over_home_23 = newImage(/*URL*/'${resourcesPath}/images/header/database-over.gif');
		over_home_27 = newImage(/*URL*/'${resourcesPath}/images/header/security-over.gif');
		over_home_28 = newImage(/*URL*/'${resourcesPath}/images/header/what-over.gif');
		over_home_29 = newImage(/*URL*/'${resourcesPath}/images/header/how-over.gif');
		over_home_30 = newImage(/*URL*/'${resourcesPath}/images/header/online-over.gif');
		over_home_31 = newImage(/*URL*/'${resourcesPath}/images/header/fact-over.gif');
		over_home_45 = newImage(/*URL*/'${resourcesPath}/images/header/site-map-over.gif');
		over_home_46 = newImage(/*URL*/'${resourcesPath}/images/header/search-over.gif');
		over_home_47 = newImage(/*URL*/'${resourcesPath}/images/header/contact-over.gif');
		preloadFlag = true;
	}
}

// --></script></head>
<body marginheight="15" marginwidth="0" topmargin="15" leftmargin="0" bgcolor="#ffffff" onload="preloadImages();">
		<div align="center">
			<span class="disclaimer">
				<table cellpadding="0" cellspacing="0" border="0" width="600">
					<tbody><tr>
						<td><a href="${servletPath}/"><img border="0" alt="" height="48" width="171" src="${resourcesPath}/images/header/netspective.gif"></a></td>
						<td>
							<table cellpadding="0" cellspacing="0" border="0">
								<tbody><tr>
									<td><img alt="" height="32" width="429" src="${resourcesPath}/images/header/spacer-top.gif"></td>
								</tr>
								<tr>
									<td>
										<table cellpadding="0" cellspacing="0" border="0">
											<tbody><tr>
												<td><a href="${servletPath}/products/frameworks/buy"><img border="0" alt="" height="16" width="365" src="${resourcesPath}/images/header/ad-line.gif"></a></td>
												<td><a href="${vc.rootUrl}/support/documentation" onmouseout="changeImages( /*CMP*/'home_6',/*URL*/'${resourcesPath}/images/header/developers.gif');return true" onmouseover="changeImages( /*CMP*/'home_6',/*URL*/'${resourcesPath}/images/header/developers-over.gif');return true"><img alt="" border="0" height="16" width="64" src="${resourcesPath}/images/header/developers.gif" name="home_6"></a></td>
											</tr>
										</tbody></table>
									</td>
								</tr>
							</tbody></table>
						</td>
					</tr>
				</tbody></table>
				<table cellpadding="0" cellspacing="0" border="0" width="600">
					<tbody><tr>
						<td><a href="${servletPath}/products" onmouseout="changeImages( /*CMP*/'home_7',/*URL*/'${resourcesPath}/images/header/products.gif');return true" onmouseover="changeImages( /*CMP*/'home_7',/*URL*/'${resourcesPath}/images/header/products-over.gif');return true"><img alt="" border="0" height="17" width="58" src="${resourcesPath}/images/header/products.gif" name="home_7"></a></td>
						<td><a href="${servletPath}/downloads" onmouseout="changeImages( /*CMP*/'home_8',/*URL*/'${resourcesPath}/images/header/downloads.gif');return true" onmouseover="changeImages( /*CMP*/'home_8',/*URL*/'${resourcesPath}/images/header/downloads-over.gif');return true"><img alt="" border="0" height="17" width="65" src="${resourcesPath}/images/header/downloads.gif" name="home_8"></a></td>
						<td><a href="${servletPath}/products/frameworks/buy" onmouseout="changeImages( /*CMP*/'home_9',/*URL*/'${resourcesPath}/images/header/buy.gif');return true" onmouseover="changeImages( /*CMP*/'home_9',/*URL*/'${resourcesPath}/images/header/buy-over.gif');return true"><img alt="" border="0" height="17" width="30" src="${resourcesPath}/images/header/buy.gif" name="home_9"></a></td>
						<td><a href="${servletPath}/services" onmouseout="changeImages( /*CMP*/'home_13',/*URL*/'${resourcesPath}/images/header/services.gif');return true" onmouseover="changeImages( /*CMP*/'home_13',/*URL*/'${resourcesPath}/images/header/services-over.gif');return true"><img alt="" border="0" height="17" width="50" src="${resourcesPath}/images/header/services.gif" name="home_13"></a></td>
						<td><a href="${servletPath}/support" onmouseout="changeImages( /*CMP*/'home_14',/*URL*/'${resourcesPath}/images/header/support.gif');return true" onmouseover="changeImages( /*CMP*/'home_14',/*URL*/'${resourcesPath}/images/header/support-over.gif');return true"><img alt="" border="0" height="17" width="50" src="${resourcesPath}/images/header/support.gif" name="home_14"></a></td>
						<td><a href="${servletPath}/company" onmouseout="changeImages( /*CMP*/'home_15',/*URL*/'${resourcesPath}/images/header/company.gif');return true" onmouseover="changeImages( /*CMP*/'home_15',/*URL*/'${resourcesPath}/images/header/company-over.gif');return true"><img alt="" border="0" height="17" width="58" src="${resourcesPath}/images/header/company.gif" name="home_15"></a></td>
						<td><img alt="" height="17" width="289" src="${resourcesPath}/images/header/rule-menu.gif" name="home_16"></td>
					</tr>
				</tbody></table>

