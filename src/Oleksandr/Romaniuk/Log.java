package Oleksandr.Romaniuk;

import javax.swing.SwingUtilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log
{
	private final Logger logger = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());

	
	    private FileHandler fh = null;
	    private static Log instanceLog = null;
	    
	    public static Log getLog()
	    {
	    	
	    	if(instanceLog == null)
	    	{
				instanceLog = new Log();
	    		return instanceLog;
	    	}
	    	else
	    	{
	    		return instanceLog;
	    	}
	    }
	    
	    private Log() 
	    {
	      
	        SimpleDateFormat format = new SimpleDateFormat("M-d_HHmmss");
	    
	        try 
	        {
	            fh = new FileHandler("C:\\Users\\Oleksandr Romaniuk\\eclipse-workspace\\ProjectJDBC\\Logger\\MyLogger.log", 1000000000, 1, true);
	        } catch (Exception e) 
	        {
	            System.out.println(e.getMessage());
	        }

	        fh.setFormatter(new Formatter() 
	        {
	            @Override
	            public String format(LogRecord record) 
	            {
	                SimpleDateFormat logTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
	                Calendar cal = new GregorianCalendar();
	                cal.setTimeInMillis(record.getMillis());
	                return record.getLevel()+" "
	                        + logTime.format(cal.getTime())
	                        + " || "
	                        + record.getSourceClassName().substring(
	                                record.getSourceClassName().lastIndexOf(".")+1,
	                                record.getSourceClassName().length())
	                        + "."
	                        + record.getSourceMethodName()
	                        + "() : "
	                        + record.getMessage() + "\n";
	            }
	        });
	        
	        fh.setFilter((Filter) new Filter()
	        		{

						@Override
						public boolean isLoggable(LogRecord record)
						{
							return record.getLevel().intValue() >= Level.INFO.intValue();
						}
	        	
	        		});
	        
	        logger.addHandler(fh);
	    }
	    	
	    
	    public void doLogging(Level level, String message)
	    {
	    	logger.log(level, message+"\n");
	    	if(level.equals(Level.SEVERE)||level.equals(Level.WARNING))
            {
            	JavaInterface.getInstance().textUpdate(message+"\n");
            }
	    }
	    
	    public void doLogging(Level level, Exception e) 
	    {
	    	logger.log(level, e.getMessage()+"\n");
	    	if(level.equals(Level.SEVERE)||level.equals(Level.WARNING))
            {
            	JavaInterface.getInstance().textUpdate(e.getMessage()+"\n");
            }
	    }
}