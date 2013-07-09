package com.cepmuvakkit.times.settings;
/*package com.cepmuvakkit.times;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Update
{
  private VakitDB dbVakit;

  Update(VakitDB paramVakitDB)
  {
    this.dbVakit = paramVakitDB;
    paramVakitDB.open();
    Il[] arrayOfIl = paramVakitDB.illerimListesi();
    paramVakitDB.close();
    for (int i = 0; ; i++)
    {
      if (i >= arrayOfIl.length)
        return;
      guncelleIl(arrayOfIl[i]);
    }
  }

  Update(VakitDB paramVakitDB, Il paramIl)
  {
    this.dbVakit = paramVakitDB;
    guncelleIl(paramIl);
  }

  public void guncelleIl(Il paramIl)
  {
    File localFile = Araclar.DownloadFromUrl("http://namazvakitleri.android.simsekburak.com/v2.0.0/guncelle.php?ulke=" + paramIl.getUlke().replace(" ", "%20") + "&il=" + paramIl.getIl().replace(" ", "%20"), "data.xml");
    if ((localFile == null) || (localFile == null));

      NodeList localNodeList = null;
	try {
		localNodeList = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new FileInputStream("/data/data/com.simsekburak.android.namazvakitleri/data.xml")).getDocumentElement().getElementsByTagName("gun");
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SAXException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ParserConfigurationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      this.dbVakit.open();
      for (int i = 0; ; i++)
      {
        int j = localNodeList.getLength();
        if (i >= j)
        {
          this.dbVakit.close();
          if (!localFile.delete())
            break;
          break;
        }
        Element localElement = (Element)localNodeList.item(i);
        String str1 = localElement.getAttribute("tarih");
        String str2 = localElement.getAttribute("imsak");
        String str3 = localElement.getAttribute("gunes");
        String str4 = localElement.getAttribute("ogle");
        String str5 = localElement.getAttribute("ikindi");
        String str6 = localElement.getAttribute("aksam");
        String str7 = localElement.getAttribute("yatsi");
        this.dbVakit.ekleVakit(paramIl.getUlke(), paramIl.getIl(), str1, new String[] { str2, str3, str4, str5, str6, str7 });
      }
    }
   
  }

 Location:           D:\eclipse\other\temproray\simsekburak.namazvakitleri-dex2jar.jar
 * Qualified Name:     com.simsekburak.android.namazvakitleri.Update
 * JD-Core Version:    0.6.2
 */