package ro.mastermind.logmonit.parsers;

import java.util.HashMap;
import java.util.Map;
import ro.mastermind.logmonit.enums.FileType;

/**
 * Factory class for different file parsers
 * @author radulescu
 */
public class FileParserFactory {
    
    public FileParser createFileParser( FileType fileType ) {
	FileParserFactoryCommand factory = factoryMap.get( fileType );
	
	if ( factory == null ) {
	    
	}
	return factory.execute();
    }
    
    private static interface FileParserFactoryCommand {
	public FileParser execute( );
    }
    
    private static Map<FileType, FileParserFactoryCommand> factoryMap = new HashMap<FileType, FileParserFactoryCommand>( );
    
    static {
	factoryMap.put( FileType.PERFORMANCE, new FileParserFactoryCommand() {
	    public FileParser execute( ) {
		return new WebPerformanceFileParser( );
	    }
	});
	
	factoryMap.put( FileType.MAIL, new FileParserFactoryCommand() {
	    public FileParser execute( ) {
		return new MailFileParser( );
	    }
	});
	
	factoryMap.put( FileType.APPLICATION, new FileParserFactoryCommand() {
	    public FileParser execute( ) {
		return new ApplicationFileParser();
	    }
	});
	
	factoryMap.put( FileType.WEB, new FileParserFactoryCommand() {
	    public FileParser execute( ) {
		return new WebFileParser();
	    }
	});
    }
    
}
