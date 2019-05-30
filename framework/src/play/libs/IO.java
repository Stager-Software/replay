package play.libs;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import play.exceptions.UnexpectedException;
import play.utils.OrderSafeProperties;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.io.IOUtils.closeQuietly;

/**
 * IO utils
 */
public class IO {

    /**
     * Read a properties file with the utf-8 encoding
     * 
     * @param is
     *            Stream to properties file
     * @return The Properties object
     */
    public static Properties readUtf8Properties(InputStream is) {
        Properties properties = new OrderSafeProperties();
        try {
            properties.load(is);
            return properties;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(is);
        }
    }

    /**
     * Read the Stream content as a string (use utf-8)
     * 
     * @param is
     *            The stream to read
     * @return The String content
     */
    public static String readContentAsString(InputStream is) {
        return readContentAsString(is, UTF_8);
    }

    /**
     * Read the Stream content as a string
     * 
     * @param is
     *            The stream to read
     * @param encoding
     *            Encoding to used
     * @return The String content
     */
    public static String readContentAsString(InputStream is, Charset encoding) {
        try {
            return IOUtils.toString(is, encoding);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Read file content to a String (always use utf-8)
     * 
     * @param file
     *            The file to read
     * @return The String content
     */
    public static String readContentAsString(File file) {
        return readContentAsString(file, "utf-8");
    }

    /**
     * Read file content to a String
     * 
     * @param file
     *            The file to read
     * @param encoding
     *            Encoding to used
     * @return The String content
     */
    public static String readContentAsString(File file, String encoding) {
        try {
            return FileUtils.readFileToString(file, encoding);
        } catch (IOException e) {
            throw new UnexpectedException(e);
        }
    }

    public static List<String> readLines(InputStream is, Charset encoding) {
        try {
           return IOUtils.readLines(is, encoding);
        }
        catch (IOException ex) {
            throw new UnexpectedException(ex);
        }
    }

    public static List<String> readLines(File file, Charset encoding) {
        try {
            return FileUtils.readLines(file, encoding);
        }
        catch (IOException ex) {
            throw new UnexpectedException(ex);
        }
    }

    /**
     * Read binary content of a file (warning does not use on large file !)
     * 
     * @param file
     *            The file te read
     * @return The binary data
     */
    public static byte[] readContent(File file) {
        try {
            return FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            throw new UnexpectedException(e);
        }
    }

    /**
     * Write String content to a file (always use utf-8)
     * 
     * @param content
     *            The content to write
     * @param file
     *            The file to write
     * @param encoding
     *            Encoding to used
     */
    public static void writeContent(CharSequence content, File file, String encoding) {
        try {
            FileUtils.write(file, content, encoding);
        } catch (IOException e) {
            throw new UnexpectedException(e);
        }
    }

    /**
     * Write binary data to a file
     * 
     * @param data
     *            The binary data to write
     * @param file
     *            The file to write
     */
    public static void write(byte[] data, File file) {
        try {
            FileUtils.writeByteArrayToFile(file, data);
        } catch (IOException e) {
            throw new UnexpectedException(e);
        }
    }

    /**
     * Copy an stream to another one.
     * 
     * @param is
     *            The source stream
     * @param os
     *            The destination stream
     */
    public static void copy(InputStream is, OutputStream os) {
        try {
            IOUtils.copyLarge(is, os);
        } catch (IOException e) {
            throw new UnexpectedException(e);
        } finally {
            closeQuietly(is);
        }
    }

    /**
     * Copy an stream to another one.
     * 
     * @param is
     *            The source stream
     * @param os
     *            The destination stream
     */
    public static void write(InputStream is, OutputStream os) {
        try {
            IOUtils.copyLarge(is, os);
        } catch (IOException e) {
            throw new UnexpectedException(e);
        } finally {
            closeQuietly(is);
            closeQuietly(os);
        }
    }

    /**
     * Copy an stream to another one.
     * 
     * @param is
     *            The source stream
     * @param f
     *            The destination file
     */
    public static void write(InputStream is, File f) {
        try {
            OutputStream os = new FileOutputStream(f);
            try {
                IOUtils.copyLarge(is, os);
            } finally {
                closeQuietly(is);
                closeQuietly(os);
            }
        } catch (IOException e) {
            throw new UnexpectedException(e);
        }
    }

    // If targetLocation does not exist, it will be created.
    public static void copyDirectory(File source, File target) {
        if (source.isDirectory()) {
            if (!target.exists()) {
                target.mkdir();
            }
            for (String child : source.list()) {
                copyDirectory(new File(source, child), new File(target, child));
            }
        } else {
            try {
                write(new FileInputStream(source), new FileOutputStream(target));
            } catch (IOException e) {
                throw new UnexpectedException(e);
            }
        }
    }

}
