package CsProject;

/*
 * Copyright (c) 1995, 2013, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

import java.net.*;
import java.io.*;

public class MultiServerThread extends Thread {
    private Socket socket = null;

    public MultiServerThread(Socket socket) {
        super("MultiServerThread");
        this.socket = socket;
    }
    
    public void run() {

        try (
	        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

        	ObjectInputStream in = new ObjectInputStream(socket.getInputStream());) {
            Message outputLine;
            Object output; 
            Object obj = null;
            Message message = null;
            ServerProtocol kkp = new ServerProtocol();
            outputLine = kkp.processInput(message);
            
            
            out.writeObject(outputLine);

           
            while ((obj = in.readObject()) != null) {
            	
     			
				Message messagein = Message.class.cast(obj);
				
				output =  kkp.processInput(messagein);
                
    
                out.writeObject(output);
                
                
                if (outputLine.equals("Bye"))
                    break;
            }
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
