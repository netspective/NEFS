 Once defined in either XML or Java, each form becomes a Dialog Object that persists 
 throughout each servlet request. This design significantly improves performance 
 because form construction is required only once during the execution of the 
 application. Because every dialog ends up as a Java object it means that the XML 
 can define the basic behavior that can later be extended by Java.