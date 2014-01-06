package ro.mastermind.logmonit.enums;

/**
 * Enum class for defining different types of files
 * @author radulescu
 */
public enum FileType {
    PERFORMANCE, WEB, APPLICATION, MAIL;
    
    public static FileType forName( String filename ) {
	if ( filename.contains("ejb-app")) {
	    return FileType.APPLICATION;
	} else if ( filename.contains("ejb-mail")) {
	    return FileType.MAIL;
	} else if ( filename.contains("performance")) {
	    return FileType.PERFORMANCE;
	} else if ( filename.contains("portal-app")) {
	    return FileType.WEB;
	}
	
	return null;
    }
}
