import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.chain.ChainMapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class MRJobChaining {

	public static class ReviewInputMapper extends Mapper<LongWritable, Text, LongWritable, Text> {

		public void map(LongWritable key , Text value , Context context) throws IOException , InterruptedException {

			String text = value.toString().trim();
			String[] split = text.split(",");
			String splitValue =  text.substring(split[0].length() + 1, text.length());
			context.write(new LongWritable(Long.parseLong(split[0])), new Text(splitValue));

		}
	}

	public static class LowerCaseMapper extends Mapper<LongWritable, Text, LongWritable, Text> {

		public void map(LongWritable key , Text value , Context context) throws IOException , InterruptedException {

			String outValue = value.toString().toLowerCase().trim();
			context.write(key, new Text(outValue));

		}
	}

	public static class SpellCheckerMapper extends Mapper<LongWritable, Text, LongWritable, Text> {

		List<SpellCheckerWritable> list = new ArrayList<SpellCheckerWritable>();

	    @Override
	    protected void setup(Context context) throws IOException, InterruptedException {

	    	String line;
	    	File spellCheckerFile = new File("spell_checker");
	        FileInputStream fis = new FileInputStream(spellCheckerFile);
	        BufferedReader cacheReader = new BufferedReader(new InputStreamReader(fis));
	    	try {
	    		while((line = cacheReader.readLine()) != null ) {

	    			String[] strArray = line.split(",");
	    			SpellCheckerWritable spellChkObj = new SpellCheckerWritable();
	    			spellChkObj.setCorrectWord(strArray[0]);
	    			spellChkObj.setIncorrectWord(strArray[1]);
	    			list.add(spellChkObj);

	    		}
	    	} catch(Exception e) {

	    		System.out.println("Exception occured while Spell Checking.");

	    	} finally {

	    		line = null;
	    		spellCheckerFile = null;
	    		fis = null;
	    		cacheReader = null;
	    	}
	    }

		public void map(LongWritable key , Text value , Context context) throws IOException , InterruptedException {

			String tempValue = value.toString().trim();
			System.out.println(tempValue);
			for(SpellCheckerWritable obj : list) {
			   if(tempValue.contains(obj.getIncorrectWord())){
				   tempValue = tempValue.replace(obj.getIncorrectWord(),obj.getCorrectWord());
			   }
			}
			context.write(key, new Text(tempValue));
		}

		@Override
	    protected void cleanup(Context context) throws IOException, InterruptedException {

			list = null ;

		}
	}


	public static class StopWordsMapper extends Mapper<LongWritable, Text, LongWritable, Text> {

		List<String> list = new ArrayList<String>();

	    @Override
	    protected void setup(Context context) throws IOException, InterruptedException {

	    	String line;
	    	File stopWordsFile = new File("stop_words");
	        FileInputStream fis = new FileInputStream(stopWordsFile);
	        BufferedReader cacheReader = new BufferedReader(new InputStreamReader(fis));
	    	try {
	    		while((line = cacheReader.readLine()) != null ) {

	    			list.add(line);

	    		}
	    	} catch(Exception e) {

	    		System.out.println("Exception occured while Stop Word removal.");

	    	} finally {

	    		line = null;
	    		stopWordsFile = null;
	    		fis = null;
	    		cacheReader = null;
	    	}
	    }

		public void map(LongWritable key , Text value , Context context) throws IOException , InterruptedException {

			String tempValue = value.toString().trim();
			for(String str : list) {
			   if(tempValue.contains(str)){
				   String regex = "\\s*\\b" + str + "\\b\\s*";
				   tempValue = tempValue.replaceAll(regex, " ");
				   tempValue = tempValue.replaceAll("\\p{P}", "");
				   tempValue = tempValue.replaceAll("[^a-zA-Z ]", "");
			   }
			}
			context.write(key, new Text(tempValue));
		}

		@Override
	    protected void cleanup(Context context) throws IOException, InterruptedException {

			list = null ;

		}
	}

	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf , "MRJobChaining");
		job.setJarByClass(MRJobChaining.class);

		/*job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setMapperClass(WhiteSpaceRemovalMapper.class);
		job.setMapOutputKeyClass(NullWritable.class);
	    job.setMapOutputValueClass(Text.class);*/
		/*
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);*/
		job.setNumReduceTasks(0);

		GenericOptionsParser gop = new GenericOptionsParser(conf , args);
		String[] inputArgs = gop.getRemainingArgs();


		FileInputFormat.addInputPath(job, new Path(inputArgs[0]));

		//MultipleInputs.addInputPath(job, new Path(inputArgs[0]),TextInputFormat.class, ReviewInputMapper.class);
		//MultipleInputs.addInputPath(job, new Path(inputArgs[1]),TextInputFormat.class, SpellCheckerInputMapper.class);
		//MultipleInputs.addInputPath(job, new Path(inputArgs[2]),TextInputFormat.class, SpellCheckerMapper.class);

		job.addCacheFile(new URI("hdfs://localhost:54310/spell_checker#spell_checker"));
		//job.addCacheFile(new URI("hdfs://localhost:54310/stop_words#stop_words"));

		Configuration mapAConf = new Configuration(false);
        ChainMapper.addMapper(job, ReviewInputMapper.class, LongWritable.class, Text.class, LongWritable.class, Text.class, mapAConf);

        Configuration mapBConf = new Configuration(false);
        ChainMapper.addMapper(job, LowerCaseMapper.class, LongWritable.class, Text.class, LongWritable.class, Text.class, mapBConf);

        Configuration mapCConf = new Configuration(false);
        ChainMapper.addMapper(job, SpellCheckerMapper.class, LongWritable.class, Text.class, LongWritable.class, Text.class, mapCConf);

        /*Configuration mapDConf = new Configuration(false);
        ChainMapper.addMapper(job, StopWordsMapper.class, LongWritable.class, Text.class, LongWritable.class, Text.class, mapDConf);*/


		FileOutputFormat.setOutputPath(job, new Path(inputArgs[1]));

		System.exit(job.waitForCompletion(true)?0:1);
	}

		public static class SpellCheckerWritable implements Writable {

			private String correctWord = null;
			private String incorrectWord = null;

			public String getCorrectWord() {
				return correctWord;
			}

			public void setCorrectWord(String correctWord) {
				this.correctWord = correctWord;
			}

			public String getIncorrectWord() {
				return incorrectWord;
			}

			public void setIncorrectWord(String incorrectWord) {
				this.incorrectWord = incorrectWord;
			}

			@Override
			public void readFields(DataInput in) throws IOException {
				correctWord = new String(in.readLine());
				incorrectWord = new String(in.readLine());
			}

			@Override
			public void write(DataOutput out) throws IOException {
				out.writeChars(correctWord);
				out.writeChars(incorrectWord);
			}

	}

}
