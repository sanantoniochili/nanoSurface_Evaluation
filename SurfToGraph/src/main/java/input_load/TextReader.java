package input_load;

import java.io.*;

public class TextReader {

    RandomAccessFile raf;
    long fp;

    public TextReader(String filename) throws FileNotFoundException {
        raf = new RandomAccessFile(filename, "r");
        fp = 0;
    }

    public String SurfToString() throws IOException {
    // reading each surface-text per call
    // setting file pointer at the end of token text
    // returns null if EOF reached
        if( fp==-1 ) return null;
        raf.seek(fp);

        int ich,temp;
        String str = "";

        while ( ((char)(ich = raf.read()) == '\n') && (ich!=-1) ); // find start of text
        if( ich==-1 ) return null; // reached end of input
        str += (char)ich; // add character to string
        temp = ich;
        while ( (temp!='\n' || (char)ich!='\n') && (ich!=-1)) {
            temp = ich; // check if there is empty line
            ich = raf.read();
            if( (char)ich!='\n' )str += (char)ich;
        }

        fp = raf.getFilePointer();
        if( fp==-1 ) return null;
        else return str;
    }
}
