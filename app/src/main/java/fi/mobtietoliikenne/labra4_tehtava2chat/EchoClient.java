package fi.mobtietoliikenne.labra4_tehtava2chat;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

//määritellään rajapinta, jonka metodien avulla välitetään viestiä.
interface EchoClientInterface{
    void onMessage(String message); //Viestin välitys
    void onStatusChange(String newStatus); //Socketin tilan tarkistus
}

public class EchoClient extends WebSocketClient {
    EchoClientInterface observer;

    public EchoClient(URI serverUri, EchoClientInterface observer) {
        super(serverUri);
        this.observer = observer;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        observer.onStatusChange("Connection Open");
    }

    @Override
    public void onMessage(String message) {
        observer.onStatusChange("New Message" +message);
        observer.onMessage(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        observer.onStatusChange("Connection Closed");
    }

    @Override
    public void onError(Exception exception) {
        observer.onStatusChange("ERROR! Something went Wrong, check Log!" +exception.toString());
    }
}
