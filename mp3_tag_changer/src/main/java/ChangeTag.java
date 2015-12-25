import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.TagField;

/**
 * @author mnikolaev
 */
public class ChangeTag {
    private static final Logger LOG = Logger.getLogger(ChangeTag.class);

    public static void main(String[] args)
            throws IOException, TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException {

        AudioFile neboFile = AudioFileIO.read(new File("f:\\music\\Пилот\\Il'ya Chert (sol'no)\\2006 - Vol'naya ptica\\01. Il'ya Chert - Nebo.mp3" ));
        AudioFile corabliFile = AudioFileIO.read(new File("f:\\music\\Сурганова\\081 Surganova i Orkestr - Korabli.mp3" ));
        Tag tag = neboFile.getTag();
        Iterator<TagField> fields = tag.getFields();
        while (fields.hasNext()) {
            TagField next = fields.next();
            System.out.println(next.getId() + "   " + new String(next.getRawContent()));
        }
        AudioHeader audioHeader = neboFile.getAudioHeader();

    }
}
