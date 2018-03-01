package jupiterpa.course.domain.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.web.multipart.MultipartFile;
import com.opencsv.CSVReader;

public class ReadUtility<T> {
    private static final Marker CONTENT = MarkerFactory.getMarker("CONTENT");
	private final Logger logger = LoggerFactory.getLogger(this.getClass());	

	public Collection<T> read(MultipartFile file, CheckedFunction<List<String>,T> create) throws IOException, FormatException {
		CSVReader csvReader = init(file); 
		
		Collection<T> list = new ArrayList<T>();
		String[] nextRecord;
		int i = 0;
		while ((nextRecord = csvReader.readNext()) != null) {
			List<String> args = new ArrayList<>(Arrays.asList(nextRecord));
			logger.info(CONTENT,"Line {} with content {}",i,args);
			args.add(Integer.toString(i));
			T entry = create.apply(args);
			logger.info(CONTENT,entry.toString());
			list.add(entry);
			i++;
		}
		csvReader.close();
		return list;
	}
	
	private CSVReader init(MultipartFile file) throws IOException {
		InputStream stream = file.getInputStream();
		Reader reader = new InputStreamReader(stream);
		CSVReader csvReader = new CSVReader(reader, ';');
		if (csvReader.readNext() == null) { // Header line
			throw new IOException();
		}
		return csvReader;
	}
	
}
