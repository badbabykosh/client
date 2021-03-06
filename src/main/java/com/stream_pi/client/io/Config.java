/*
Config.java

Contributor(s) : Debayan Sutradhar (@rnayabed)

handler for config.xml
 */

package com.stream_pi.client.io;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.stream_pi.client.info.ClientInfo;
import com.stream_pi.util.exception.SevereException;
import com.stream_pi.util.platform.Platform;
import com.stream_pi.util.xmlconfighelper.XMLConfigHelper;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Config {
    private static Config instance = null;

    private final File configFile;

    private Document document;

    private Config() throws SevereException
    {
        try
        {
            configFile = new File(ClientInfo.getInstance().getPrePath()+"config.xml");
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            document = docBuilder.parse(configFile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new SevereException("Config", "unable to read config.xml");
        }
    }

    public static synchronized Config getInstance() throws SevereException
    {
        if(instance == null)
            instance = new Config();

        return instance;
    }

    public void save() throws SevereException
    {
        try
        {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Result output = new StreamResult(configFile);
            Source input = new DOMSource(document);

            transformer.transform(input, output);
        }
        catch (Exception e)
        {
            throw new SevereException("Config", "unable to save config.xml");
        }
    }


    //Client Element
    public Element getClientElement()
    {
        return (Element) document.getElementsByTagName("client").item(0);
    }
    
    //Default Values
    public String getDefaultClientNickName()
    {
        return "StreamPi Client";
    }

    public String getDefaultStartupProfileID()
    {
        return "default";
    }

    public String getDefaultCurrentThemeFullName()
    {
        return "com.StreamPi.DefaultLight";
    }

    public String getDefaultThemesPath()
    {
        return "Themes/";
    }

    public String getDefaultProfilesPath()
    {
        return "Profiles/";
    }

    public String getDefaultIconsPath()
    {
        return "Icons/";
    }

    public int getDefaultStartupWindowWidth()
    {
        return 800;
    }

    public int getDefaultStartupWindowHeight()
    {
        return 400;
    }

    //Getters

    public String getClientNickName()
    {
        return XMLConfigHelper.getStringProperty(getClientElement(), "nickname", getDefaultClientNickName(), false, true, document, configFile);
    }

    public String getStartupProfileID()
    {
        return XMLConfigHelper.getStringProperty(getClientElement(), "startup-profile", getDefaultStartupProfileID(), false, true, document, configFile);
    }

    public String getCurrentThemeFullName()
    {
        return XMLConfigHelper.getStringProperty(getClientElement(), "current-theme-full-name", getDefaultCurrentThemeFullName(), false, true, document, configFile);
    }

    public String getThemesPath()
    {
        if(ClientInfo.getInstance().getPlatformType() == Platform.ANDROID)
            return ClientInfo.getInstance().getPrePath() + "Themes/";
        
        return XMLConfigHelper.getStringProperty(getClientElement(), "themes-path", getDefaultThemesPath(), false, true, document, configFile);
    }

    public String getProfilesPath()
    {

        if(ClientInfo.getInstance().getPlatformType() == Platform.ANDROID)
            return ClientInfo.getInstance().getPrePath() + "Profiles/";
        
        return XMLConfigHelper.getStringProperty(getClientElement(), "profiles-path", getDefaultThemesPath(), false, true, document, configFile);
    }

    public String getIconsPath()
    {
        if(ClientInfo.getInstance().getPlatformType() == Platform.ANDROID)
            return ClientInfo.getInstance().getPrePath() + "Icons/";
        
        return XMLConfigHelper.getStringProperty(getClientElement(), "icons-path", getDefaultThemesPath(), false, true, document, configFile);
    }

    public double getStartupWindowWidth()
    {
        return XMLConfigHelper.getDoubleProperty((Element) getClientElement().getElementsByTagName("startup-window-size").item(0), "width", getDefaultStartupWindowWidth(), false, true, document, configFile);
    }

    public double getStartupWindowHeight()
    {
        return XMLConfigHelper.getDoubleProperty((Element) getClientElement().getElementsByTagName("startup-window-size").item(0), "height", getDefaultStartupWindowHeight(), false, true, document, configFile);
    }

    

    //Setters

    public void setNickName(String nickName)
    {
        getClientElement().getElementsByTagName("nickname").item(0).setTextContent(nickName);
    }

    public void setStartupProfileID(String id)
    {
        getClientElement().getElementsByTagName("startup-profile").item(0).setTextContent(id);
    }

    public void setCurrentThemeFullName(String name)
    {
        getClientElement().getElementsByTagName("current-theme-full-name").item(0).setTextContent(name);
    }

    public void setProfilesPath(String profilesPath)
    {
        getClientElement().getElementsByTagName("profiles-path").item(0).setTextContent(profilesPath);
    }

    public void setIconsPath(String iconsPath)
    {
        getClientElement().getElementsByTagName("icons-path").item(0).setTextContent(iconsPath);
    }

    public void setThemesPath(String themesPath)
    {
        getClientElement().getElementsByTagName("themes-path").item(0).setTextContent(themesPath);
    }

    //client > startup-window-size
    public void setStartupWindowSize(double width, double height)
    {
        setStartupWindowWidth(width);
        setStartupWindowHeight(height);
    }

    public void setStartupWindowWidth(double width)
    {
        ((Element) getClientElement().getElementsByTagName("startup-window-size").item(0))
            .getElementsByTagName("width").item(0).setTextContent(width+"");
    }

    public void setStartupWindowHeight(double height)
    {
        ((Element) getClientElement().getElementsByTagName("startup-window-size").item(0))
            .getElementsByTagName("height").item(0).setTextContent(height+"");
    }











    //comms-server
    public Element getCommsServerElement()
    {
        return (Element) document.getElementsByTagName("comms-server").item(0);
    }

    public String getDefaultSavedServerHostNameOrIP()
    {
        return "127.0.0.1";
    }

    public int getDefaultSavedServerPort()
    {
        return -1;
    }


    public String getSavedServerHostNameOrIP()
    {
        return XMLConfigHelper.getStringProperty(getCommsServerElement(), "hostname-ip", getDefaultSavedServerHostNameOrIP(), false, true, document, configFile);
    }

    public int getSavedServerPort()
    {
        return XMLConfigHelper.getIntProperty(getCommsServerElement(), "port", getDefaultSavedServerPort(), false, true, document, configFile);
    }


    public void setServerHostNameOrIP(String hostNameOrIP)
    {
        getCommsServerElement().getElementsByTagName("hostname-ip").item(0).setTextContent(hostNameOrIP);
    }

    public void setServerPort(int port)
    {
        getCommsServerElement().getElementsByTagName("port").item(0).setTextContent(port+"");
    }





    


    //others
    public Element getOthersElement()
    {
        return (Element) document.getElementsByTagName("others").item(0);
    }

    //others-default

    public boolean getDefaultStartOnBoot()
    {
        return false;
    }

    public boolean getDefaultFullscreen()
    {
        return true;
    }

    public boolean getDefaultIsShowCursor()
    {
        return true;
    }

    public boolean getDefaultFirstTimeUse()
    {
        return true;
    }


    
    public boolean isShowCursor()
    {
        return XMLConfigHelper.getBooleanProperty(getOthersElement(), "show-cursor", getDefaultIsShowCursor(), false, true, document, configFile);
    }

    
    public boolean isStartOnBoot()
    {
        return XMLConfigHelper.getBooleanProperty(getOthersElement(), "start-on-boot", getDefaultStartOnBoot(), false, true, document, configFile);
    }


    public boolean isFullscreen()
    {
        return XMLConfigHelper.getBooleanProperty(getOthersElement(), "fullscreen", getDefaultFullscreen(), false, true, document, configFile);
    }

    
    public boolean isFirstTimeUse()
    {
        return XMLConfigHelper.getBooleanProperty(getOthersElement(), "first-time-use", true, false, true, document, configFile);
    }


    public void setStartOnBoot(boolean value)
    {
        getOthersElement().getElementsByTagName("start-on-boot").item(0).setTextContent(value+"");
    }

    public void setShowCursor(boolean value)
    {
        getOthersElement().getElementsByTagName("show-cursor").item(0).setTextContent(value+"");
    }

    public void setFullscreen(boolean value)
    {
        getOthersElement().getElementsByTagName("fullscreen").item(0).setTextContent(value+"");
    }

    public void setFirstTimeUse(boolean value)
    {
        getOthersElement().getElementsByTagName("first-time-use").item(0).setTextContent(value+"");
    }

}
