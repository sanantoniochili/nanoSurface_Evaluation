package gr.demokritos.iit.s2graph.ngramgraphs;

import java.io.*;

/**
 * Reads the text that is produced by surface encoding.
 * <br>Each surface is represented as text and its title is the parameters that characterize the surface.
 */
public class TextReader {

    /**
     * Input file
     */
    RandomAccessFile raf;
    /**
     * Surface parameters
     */
    String[] params;
    /**
     * File pointer
     */
    long fp;

    /**
     * <p>Initialization</p>
     *
     * @param filename          Name of input file
     * @throws FileNotFoundException
     */
    public TextReader(String filename) throws FileNotFoundException {
        raf = new RandomAccessFile(filename, "r");
        fp = 0;
    }

    /**
     * <p>Reads each surface-text per call. Sets file pointer at the end of token text. Returns null if EOF reached</p>
     *
     * @return              String describing surface
     * @throws IOException
     */
    // new to include parameters
    public String SurfToString() throws IOException {
        if( fp==-1 ) return null;
        raf.seek(fp);

        int ich,temp;
        String str = "";

        while ( ((char)(ich = raf.read()) == '\n') && (ich!=-1) ); // find start of text
        if( ich==-1 ) return null; // reached end of input

        // read parameters
        String buffer = "";
        String split = ":";
        do{
            buffer += (char)ich;
        }while ( ((char)(ich = raf.read()) != '\n') && (ich!=-1) );

        // save parameter values as strings
        this.params = buffer.split(split);

        // read text
        ich = (char)raf.read();
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

    /**
     * @return          Surface parameters
     */
    public String[] getParams() {
        return params;
    }
}
