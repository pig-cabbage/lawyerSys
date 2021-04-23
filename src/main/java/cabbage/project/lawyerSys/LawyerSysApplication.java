package cabbage.project.lawyerSys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LawyerSysApplication {

//  public static void startWebsocketInstantMsg() {
//    WebSocketImpl.DEBUG = false;
//    MyWebSocket s;
//    InetSocketAddress inetSocketAddress = new InetSocketAddress(8888);
//    s = new MyWebSocket(inetSocketAddress);
//    s.start();
//    System.out.println("websocket启动成功");
//  }

  public static void main(String[] args) {
//    startWebsocketInstantMsg();
    SpringApplication.run(LawyerSysApplication.class, args);
  }

}
