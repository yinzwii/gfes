package com.browser;

import com.browser.browser.MyCefBrowser;
import com.browser.handler.DownloadHandler;
import com.browser.handler.MenuHandler;
import com.browser.handler.MessageRouterHandler;
import com.browser.service.MyService;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.browser.CefMessageRouter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@Component
public class BrowserWindow implements InitializingBean {


    private JFrame jFrame;

    private MyService myService;
    private MyCefBrowser myCefBrowser;
    String furl;

    public BrowserWindow(MyService myService) {
        this.myService = myService;
    }


    public void open(String title,String url,boolean visible){
        if (this.jFrame == null){
            init(url);
            this.jFrame.setTitle(title);
            this.furl = url;
            jFrame.setExtendedState(JFrame.NORMAL);
            jFrame.setVisible(visible);
            return;
        }
        this.jFrame.setTitle(title);
        this.furl = url;
        jFrame.setExtendedState(JFrame.NORMAL);
        this.myCefBrowser.getBrowser().loadURL(url);
        jFrame.setVisible(visible);



    }

    private void init(String url) {
//        EventQueue.invokeLater(() -> {
            this.jFrame = new JFrame("文件预览浏览器");
            jFrame.setMinimumSize(new Dimension(1366, 738));    // 设置最小窗口大小
            jFrame.setExtendedState(JFrame.NORMAL);
            jFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(jFrame.getClass().getResource("/image/browser.png")));

            if (!CefApp.startup()) {    // 初始化失败
                JLabel error = new JLabel("<html><body>&nbsp;&nbsp;&nbsp;&nbsp;在启动这个应用程序时，发生了一些错误，请关闭并重启这个应用程序。<br>There is something wrong when this APP start up, please close and restart it.</body></html>");
                error.setFont(new Font("宋体/Arial", Font.PLAIN, 28));
                error.setIcon(new ImageIcon(jFrame.getClass().getResource("/image/error.png")));
                error.setForeground(Color.red);
                error.setHorizontalAlignment(SwingConstants.CENTER);

                jFrame.getContentPane().setBackground(Color.white);
                jFrame.getContentPane().add(error, BorderLayout.CENTER);
                return;
            }

            this.myCefBrowser = new MyCefBrowser(url, false, false);
            CefClient client = myCefBrowser.getClient();
            // 绑定 MessageRouter 使前端可以执行 js 到 java 中
            CefMessageRouter cmr = CefMessageRouter.create(new CefMessageRouter.CefMessageRouterConfig("cef", "cefCancel"));
            cmr.addHandler(new MessageRouterHandler(myService), true);
            client.addMessageRouter(cmr);
            // 绑定 ContextMenuHandler 实现右键菜单
            client.addContextMenuHandler(new MenuHandler(jFrame));
            // 绑定 DownloadHandler 实现下载功能
            client.addDownloadHandler(new DownloadHandler());

            jFrame.getContentPane().add(myCefBrowser.getBrowserUI(), BorderLayout.CENTER);
            jFrame.setVisible(false);

            jFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    jFrame.setVisible(false);
                }
            });
//        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        init("http://www.baidu.com");
    }
}
