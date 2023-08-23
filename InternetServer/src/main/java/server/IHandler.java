package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This interface defines the common functionality required for handling client requests.
 */
public interface IHandler {

    /**
     * Handles a client request by reading from the input stream and writing to the output stream.
     *
     * @param fromClient the input stream to read client data from
     * @param toClient   the output stream to write response data to
     */
    public abstract void handleClient(InputStream fromClient,
                                      OutputStream toClient) throws IOException, ClassNotFoundException;
}
