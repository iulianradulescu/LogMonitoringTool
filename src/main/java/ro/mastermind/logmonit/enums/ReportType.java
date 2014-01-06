package ro.mastermind.logmonit.enums;

import java.util.Arrays;
import java.util.List;

/**
 * Enum class for defining types of reports
 * @author radulescu
 */
public enum ReportType {
    PERFORMANCE(FileType.PERFORMANCE), ACTIVITY(FileType.WEB,FileType.APPLICATION);
    
    private List<FileType> fileTypes;
    
    ReportType( FileType ... types ) {
	fileTypes = Arrays.asList( types );
    }
    
    public boolean isAccepted( FileType type ) {
	return fileTypes.contains( type );
    } 
} 
