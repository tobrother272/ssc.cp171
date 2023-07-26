const minimist = require('minimist');
const puppeteer = require('puppeteer-core');
const GoLogin = require('gologin');
const net = require('net');
const fs = require('fs');
// const pluginStealth =require('puppeteer-extra-plugin-stealth') ;
// puppeteer.use(pluginStealth());

let args = minimist(process.argv.slice(2));
//console.log(args);


if (!args.t) {
  console.log('Không tìm thấy Token');
  return;
}
if (!args.p) {
  console.log('-p : profile id');
  return;
}
if (!args.n) {
  console.log('-n : Account Name');
  return;
}
if (!args.s) {
  console.log('-s : screen size');
  return;
}
if (!args.f) {
  console.log('-f : profileFolder');
  return;
}

const host = args.i.trim() || 'localhost';
const proxy = args.x ? args.x.trim() : '';
const token = args.t.trim();
const profile_id = args.p.trim();
const accountName = args.n.trim();
const profileFolder = args.f.trim();
const myWindoSize = args.s.trim() || '1280,720';
const location = args.l.trim() || '0,0'
const headless = args.h.trim() === "true" ? true : false;
const useragent = args.a ? args.a.trim() : ""


const CLEAR_CACHE = 'CLEAR_CACHE';
const COMMAND_LOAD = 'LOAD';
const COMMAND_GET_URL = 'GETURL';
const COMMAND_STOP = 'STOP';
const COMMAND_GET_TITLE = 'GETTITLE';
const COMMAND_CLICK = 'CLICK';
const COMMAND_TYPE = 'TYPE';
const COMMAND_SEND_FILE = 'SENDFILE';
const COMMAND_WAITELEMENTVISIBLE = 'WAITELEMENTVISIBLE';
const COMMAND_WAITELEMENTDISAPPEAR = 'WAITELEMENTDISAPPEAR';
const COMMAND_WAITJSFUNCTION = 'WAITJSFUNCTION';
const COMMAND_COUNT = 'COUNT';
const COMMAND_WAIT = 'WAIT';
const COMMAND_GETJS = 'GETJS';
const COMMAND_RUNJS = 'RUNJS';
const COMMAND_HOVER = 'HOVER';
const COMMAND_SAVECOOKIE = 'SAVECOOKIE';
const COMMAND_CAPTURE = 'CAPTURE';
const COMMAND_SELECT = 'SELECT';
const COMMAND_CHANGE_TAB = 'CHANGE_TAB';
const COMMAND_CLOSE_TAB = 'CLOSE_TAB';
const COMMAND_HIDE = 'HIDE';

const COMMAND_OPEN_NEWTAB = 'LOAD_NEWTAB';
const CONNECTED = 'connected';
const RESULT_SUCCESS = 'success';
const RESULT_FAIL = 'fail';
const RESULT_NOTFOUND = 'not_found';
const RESULT_READY = 'ready';
const RESULT_OUT_VIEWPORT = 'out_viewport';
const RESULT_INVISIBLE = 'invisible';






(async () => {
  reponseToServer = async (response) => {
    const res = {
      ...response,
      target: accountName.trim()
    }
    //console.log('reponseToServer', res);
    global.client.write(`${JSON.stringify(res)} \n`);
  };

  createGologinInstance = async () => {
    //console.log('createGologinInstance');
    await reponseToServer({
      code: 1,
      message: CONNECTED,
      profile_id,
      name: accountName.trim(),
    });

    try {
      let options = {
        token,
        profile_id,
        focus: args.u || false,
        canvasMode: args.c.trim() || 'off',
        location,
        useragent,
        username: accountName.trim(),
        headless: headless,
        executablePath: args.b.trim(),
        tmpdir: profileFolder,
        myWindoSize: args.s.trim()
      }
      if (proxy) {
        const proxyType = proxy.split(":").length
        if (proxyType === 3) {
          options = {
            ...options,
            proxy: {
              mode: proxy.split(":")[0],
              host: proxy.split(":")[1],
              port: proxy.split(":")[2]
            }
          }
        } else {
          options = {
            ...options,
            proxy: {
              mode: proxy.split(":")[0],
              host: proxy.split(":")[1],
              port: proxy.split(":")[2],
              username: proxy.split(":")[3],
              password: proxy.split(":")[4]
            }
          }
        }

      }
      console.log("options ", options);
      global.GL = new GoLogin(options);


      //   const wsUrl = await GL.startLocal();
      const { status, wsUrl } = await global.GL.startLocal();

      console.log("--------startLocal finish-------- ");
     
        global.browser = await puppeteer.connect({
              browserWSEndpoint: wsUrl.toString(), 
              ignoreHTTPSErrors: true,
                args: [
                '--ignore-certificate-errors',
                '--no-sandbox',
                '--disable-setuid-sandbox',
                '--window-size=1920,1080',
                "--disable-accelerated-2d-canvas",
                "--disable-gpu"],
          ignoreHTTPSErrors: true,
          headless: headless,
         });
        
      console.log("--------connect finish-------- ");


      const pages = await global.browser.pages();
      //get last page
      let page = pages.pop();
      global.page = page;


      //close other pages
      await pages.map((p) => p.close());
      const viewPort = await GL.getViewPort();
      //await global.browser.resize({ width:  parseInt(args.s.trim().split(",")[0]), height: parseInt(args.s.trim().split(",")[1])});
      await global.page.setViewport({ width: parseInt(args.v.trim().split("x")[0]), height: parseInt(args.v.trim().split("x")[1]) });
      //global.page.bringToFront()

      await page.goto("https://youtube.com/", {
        waitUntil: 'domcontentloaded',
        // Remove the timeout
        timeout: 0
      });
 

      //await fake();
      await reponseToServer({
        code: 1,
        message: RESULT_READY,
        profile_id,
        port: wsUrl,
        name: accountName,
      });
    } catch (e) {
      await reponseToServer({
        code: 1,
        message: "INITFAIL",
        profile_id,
        name: accountName.trim(),
      });
    }
  };

  endGologin = async (jsonData) => {
    if (global.GL) {
      await global.browser.close();
      await global.GL.stop();
      await global.GL.stopLocal({ posting: false });
      await global.client.destroy();
    }
    const { id } = jsonData;
    await reponseToServer({
      code: id,
      message: RESULT_SUCCESS
    });

  };

  scrollToAndHover = async (elm) => {
    //console.log('scrollToAndHover');
    await elm.evaluate((e) =>
      e.scrollIntoView({
        block: 'center',
        inline: 'center',
        behavior: 'smooth',
      }),
    );
    await global.page.waitForTimeout(2000);
    await elm.hover();
  };
  normalizeData = (data) => {
    try {
      let strData = data.trim();
      while (strData.charAt(0) != '{') {
        //console.log('normalizeData strData[0]', strData.charAt(0));
        strData = strData.substring(1);
        //console.log('normalizeData strData', strData);
      }
      //console.log('normalizeData strData', strData);
      const jsonData = JSON.parse(strData);
      return jsonData;
    } catch (e) {
      //console.log('normalizeData', e);
    }
    return {};
  };

  getPages = async () => {
    const pages = await global.browser.pages();
    const promiseAll = pages.map((page) => page.url());
    const pageUrls = await Promise.all(promiseAll);
    //console.log('getPages', pageUrls);
  };

  getActivePage = async () => {
    let [page] = await global.browser.pages();
    return page;
  };

  getCurrentPageUrl = async (jsonData) => {
    const { id } = jsonData;
    const currentPage = await getActivePage();
    let url = await currentPage.url();
    await reponseToServer({
      code: id,
      message: url,
    });
  };
  const saveCookie = async (jsonData) => {


    try {
      const { cookiePath, id } = jsonData;
      let [page] = await global.browser.pages();
      const cookies = await page.cookies();
      const cookieJson = JSON.stringify(cookies, null, 2);

      console.log(cookiePath)
      console.log(cookieJson)

      fs.writeFile(cookiePath, cookieJson, function (err) {
        if (err) throw err;
      });

      await reponseToServer({
        code: id,
        message: "success",
      });
    } catch (ex) {
      console.log(ex)
    }



  }
  getCurrentPageTitle = async (jsonData) => {
    const { id } = jsonData;
    const currentPage = await getActivePage();
    const title = await currentPage.title();
    await reponseToServer({
      code: id,
      message: title,
    });
  };

  fake = async () => {

    await page.addScriptTag({
      content: 'document.hasFocus = function () {return true;};',
    });
    await page.evaluate((e) => {
      document.hasFocus = function () {
        return true;
      };
    });

  }
  excuteGotoPage = async (jsonData = {}) => {
    let { id, url, timeout } = jsonData;
    try {
      let [page] = await global.browser.pages();
      if (timeout === 0) {
        try {
          page.setDefaultNavigationTimeout(0);
          await page.goto(url, {
            waitUntil: 'domcontentloaded',
            // Remove the timeout
            timeout: 0
          });
        } catch (error) {

        }
      } else {
        await page.setRequestInterception(false);
        await page.goto(url, {
          timeout: timeout || 30000,
        });

      }
      await fake();
      await reponseToServer({
        code: id,
        message: RESULT_SUCCESS,
      });
    } catch (e) {
      console.log(e)
      await reponseToServer({
        code: id,
        message: RESULT_FAIL,
        error: e.message,
      });
    }
  };




  closeTab = async (jsonData = {}) => {
    let { id, index } = jsonData;
    try {


      let pages = await global.browser.pages();
      global.page = await pages[index];

      await global.page.bringToFront();
      //await fake();
      await reponseToServer({
        code: id,
        message: RESULT_SUCCESS,
      });
    } catch (e) {
      console.log(e.message)
      await reponseToServer({
        code: id,
        message: RESULT_FAIL,
        error: e.message,
      });
    }
  };
  changeTab = async (jsonData = {}) => {
    let { id, index } = jsonData;
    try {


      let pages = await global.browser.pages();
      global.page = await pages[index];

      await global.page.bringToFront();
      //await fake();
      await reponseToServer({
        code: id,
        message: RESULT_SUCCESS,
      });
    } catch (e) {
      console.log(e.message)
      await reponseToServer({
        code: id,
        message: RESULT_FAIL,
        error: e.message,
      });
    }
  };


  excuteGotoPageNewTab = async (jsonData = {}) => {
    let { id, url, timeout } = jsonData;
    try {

      const page2 = await global.browser.newPage();
      // open new tab
      let [page] = await global.browser.pages();
      await page2.goto(url, {
        timeout: timeout || 30000,
      });   // go to github.com 
      await page2.bringToFront();                   // make the tab active
      global.page = page2;


      //await fake();
      await reponseToServer({
        code: id,
        message: RESULT_SUCCESS,
      });
    } catch (e) {
      await reponseToServer({
        code: id,
        message: RESULT_FAIL,
        error: e.message,
      });
    }
  };



  excuteCapture = async (jsonData = {}) => {
    let { id, path, timeout } = jsonData;
    try {
      let [page] = await global.browser.pages();
      await page.screenshot({                      // Screenshot the website using defined options
        path: path,                   // Save the screenshot in current directory
        fullPage: true                              // take a fullpage screenshot
      });
      const html = await page.content();
      fs.writeFile(path.replace("jpg", "html"), html, function (err) {
        if (err) throw err;
        console.log('Replaced!');
      });
      await reponseToServer({
        code: id,
        message: RESULT_SUCCESS,
      });
    } catch (e) {
      await reponseToServer({
        code: id,
        message: RESULT_FAIL,
        error: e.message,
      });
    }
  };





  executeSendFile = async (jsonData) => {
    const { xpath, id, position, file } = jsonData;
    try {
      const elm = await findElementByXpath(jsonData);
      //const elm= await global.page.$x(xpath);
      await elm.uploadFile(file);

      await reponseToServer({
        code: id,
        message: RESULT_SUCCESS,
      });
    } catch (e) {
      await reponseToServer({
        code: id,
        message: RESULT_FAIL,
        error: e.message,
      });
    }
  };

  waitPageLoad = async (jsonData = {}) => {
    let { id, timeout } = jsonData;
    try {

      //await  global.page.waitForNavigation({timeout: timeout,  waitUntil: 'Load' })
      //await  global.page.waitForNavigation({timeout: timeout,  waitUntil: 'domcontentloaded' })
      //await currentPage.waitForNavigation();
      await reponseToServer({
        code: id,
        message: RESULT_SUCCESS,
      });

    } catch (e) {
      await reponseToServer({
        code: id,
        message: RESULT_FAIL,
        error: e.message,
      });
    }
  };

  hide = async (jsonData = {}) => {
    let { id } = jsonData;
    try {

      const session = await page.target().createCDPSession();
      const { windowId } = await session.send('Browser.getWindowForTarget');
      await session.send('Browser.setWindowBounds', { windowId, bounds: { windowState: 'minimized' } });
      await reponseToServer({
        code: id,
        message: RESULT_SUCCESS,
      });

    } catch (e) {
      await reponseToServer({
        code: id,
        message: RESULT_FAIL,
        error: e.message,
      });
    }
  };



  findElementByXpath = async ({ xpath, id, position, frame }) => {
    let twitterFrame = null;
    if (frame) {
      const page = await global.page
      for (const frameA of page.mainFrame().childFrames()) {
        console.log(frameA.url())
        if (frameA.url().includes(frame)) {
          twitterFrame = frameA;
          console.log("da tim thay frame")
          break;
        }
      }
    }

    const pos = position || 0;
    let arrBtn
    if (twitterFrame) {
      arrBtn = await twitterFrame.$x(xpath);
    } else {
      arrBtn = await global.page.$x(xpath);
    }
    if (arrBtn && arrBtn.length) {
      let isVisibleHandle
      if (twitterFrame) {
        isVisibleHandle = await twitterFrame.evaluateHandle((e) => {
          const style = window.getComputedStyle(e);
          return (
            style &&
            style.display !== 'none' &&
            style.visibility !== 'hidden' &&
            style.opacity !== '0'
          );
        }, arrBtn[pos]);
      } else {
        isVisibleHandle = await page.evaluateHandle((e) => {
          const style = window.getComputedStyle(e);
          return (
            style &&
            style.display !== 'none' &&
            style.visibility !== 'hidden' &&
            style.opacity !== '0'
          );
        }, arrBtn[pos]);
      }

      var visible = await isVisibleHandle.jsonValue();
      const isInViewport = await arrBtn[pos].isIntersectingViewport();
      //console.log('findElementByXpath', visible, isInViewport);
      if (visible && isInViewport) {
        await arrBtn[pos].hover();
        return arrBtn[pos];
      }
      else if (!visible) {
        throw new Error(RESULT_INVISIBLE);
      }
      else if (!isInViewport) {
        await scrollToAndHover(arrBtn[pos]);
        throw new Error(RESULT_OUT_VIEWPORT);
      }
    } else {
      await reponseToServer({
        code: id,
        message: RESULT_NOTFOUND,
      });
      return false;
    }
  };
  runJS = async (jsonData, isReturn) => {
    const { id, JS, timeout, frame } = jsonData;
    try {
      //const [page] = await global.browser.pages();

      let twitterFrame = null;
      if (frame) {
        const page = await global.page
        for (const frameA of page.mainFrame().childFrames()) {
          if (frameA.url().includes(frame)) {
            twitterFrame = frameA;
            break;
          }
        }
      }
      let value;
      if (twitterFrame == null) {
        value = await global.page.evaluate(new Function(JS));
      } else {
        value = await twitterFrame.evaluate(new Function(JS));
      }
      if (timeout) {
        await global.page.setDefaultNavigationTimeout(timeout);
        //await page.waitForNavigation({timeout,waitUntil: ['networkidle0', 'networkidle2']});
        await global.page.waitForNavigation();
        //await page.waitForNavigation({ jsonData });
      }
      await reponseToServer({
        code: id,
        message: RESULT_SUCCESS,
        value: isReturn ? value : '',
      });
    } catch (e) {
      await reponseToServer({
        code: id,
        message: RESULT_FAIL,
        error: e.message,
      });
    }
  };
  getRandomInt = (max) => {
    return Math.floor(Math.random() * max);
  }
  waitRandom = async () => {
    await global.page.waitForTimeout(getRandomInt(5) * 300);
  }

  excuteHoverCommand = async (jsonData) => {
    const { timeout, id, xpath } = jsonData;
    try {
      const elm = await findElementByXpath(jsonData);
      if (elm) {
        //console.log('found element');
        await scrollToAndHover(elm);
        //console.log('clicked');
        waitRandom();

        await reponseToServer({
          code: id,
          message: RESULT_SUCCESS,
        });
      }
    } catch (e) {
      console.log(e.message)
      await reponseToServer({
        code: id,
        message: RESULT_FAIL,
        error: e.message,
      });
    }
  };

  excuteClickCommand = async (jsonData) => {
    const { timeout, id, xpath } = jsonData;
    try {
      const elm = await findElementByXpath(jsonData);
      if (elm) {
        waitRandom();
        //console.log('found element');
        await elm.click();
        console.log('clicked');
        waitRandom();

        if (timeout) {
          console.log('Chờ ' + timeout);
          await global.page.setDefaultNavigationTimeout(timeout);
          //await page.waitForNavigation({timeout,waitUntil: ['networkidle0', 'networkidle2']});
          await global.page.waitForNavigation();
          //await page.waitForXPath('//div[@id="movie_player"]', {timeout, visible:true});
          await fake();
        } else {
          await global.page.waitForTimeout((getRandomInt(10) + 2) * 5);
        }
        console.log('Hoàn thành excuteClickCommand');
        await reponseToServer({
          code: id,
          message: RESULT_SUCCESS,
        });
      }
    } catch (e) {

      console.log('loi click', e);

      if (e.message === RESULT_OUT_VIEWPORT) {
        console.log('click command incatch out_viewport', RESULT_OUT_VIEWPORT);
        await global.page.waitForTimeout(200);
        await excuteClickCommand(jsonData);
        return;
      }
      await reponseToServer({
        code: id,
        message: RESULT_FAIL,
        error: e.message,
      });
    }
  };

  excuteSelectCommand = async (jsonData) => {
    const { id, data, xpath } = jsonData;
    //const elm = await findElementByXpath(jsonData);
    //if (elm) {

    //await global.page.bringToFront();


    await global.page.select(xpath, data);


    reponseToServer({
      code: id,
      message: RESULT_SUCCESS,
    });
    //}
  };


  clearCache = async (jsonData) => {
    const { id } = jsonData;

    await page.keyboard.down('Control');
    await page.keyboard.press('F5');
    await page.keyboard.up('Control');
    //await page.keyboard.press('Backspace');
    await page.setCacheEnabled(false);
    await page.reload({ waitUntil: 'networkidle2' });
    reponseToServer({
      code: id,
      message: RESULT_SUCCESS,
    });

  };


  excuteTypeCommand = async (jsonData) => {
    const { id, data, delay } = jsonData;
    const elm = await findElementByXpath(jsonData);
    if (elm) {
      const { id } = jsonData;
      //await global.page.bringToFront();
      await elm.focus();
      /*
      console.log("ĐÃ focus");
      await page.keyboard.down('Control');
      await page.keyboard.press('A');
      
      await page.keyboard.up('Control');
      await page.keyboard.press('Backspace');
      console.log("ĐÃ clear");
      */

      if (data === "#Backspace") {
        await page.keyboard.press('Backspace');
      } else {
        await elm.type(data, { delay: delay });
      }

      reponseToServer({
        code: id,
        message: RESULT_SUCCESS,
      });
    }
  };

  countElement = async (jsonData) => {
    const { id, xpath, frame } = jsonData;
    try {
      console.log("frame" + frame + " id " + id)
      if (frame) {
        let twitterFrame;
        const page = await global.page
        for (const frameA of page.mainFrame().childFrames()) {
          console.log(frameA.url())
          if (frameA.url().includes(frame)) {
            twitterFrame = frameA;
            console.log("da tim thay frame")
            break;
          }
        }
        //const frame = await twitterFrame.contentFrame();
        const els = await twitterFrame.$x(xpath);
        await reponseToServer({
          code: id,
          message: RESULT_SUCCESS,
          count: els ? els.length : 0,
        });
      } else {
        const els = await global.page.$x(xpath);
        await reponseToServer({
          code: id,
          message: RESULT_SUCCESS,
          count: els ? els.length : 0,
        });
      }
    } catch (e) {
      console.log(e)
      await reponseToServer({
        code: id,
        message: RESULT_FAIL,
        error: e.message,
      });
    }
  };
  waitForElementByXpath = async (jsonData, option) => {
    const { id, xpath, timeout } = jsonData;
    //await global.page.bringToFront();
    try {
      //const [page] = await global.browser.pages();
      await global.page.waitForXPath(xpath, {
        ...option,
        timeout,
      });
      await reponseToServer({
        code: id,
        message: RESULT_SUCCESS,
      });
    } catch (e) {
      console.log(e.message)
      await reponseToServer({
        code: id,
        message: RESULT_FAIL,
        error: e.message,
      });
    }
  };
  const excuteByCommand = async (jsonData) => {
    const { xpath, id, position, command } = jsonData;
    //console.log("command hien tai "+command)
    try {
      if (command) {
        switch (command) {
          case COMMAND_CLICK:
            await excuteClickCommand(jsonData);
            break;
          case COMMAND_HOVER:
            await excuteHoverCommand(jsonData);
            break;
          case COMMAND_TYPE:
            await excuteTypeCommand(jsonData);
            break;
          case COMMAND_SELECT:
            await excuteSelectCommand(jsonData);
            break;
          case COMMAND_GET_URL:
            await getCurrentPageUrl(jsonData);
            break;
          case COMMAND_GET_TITLE:
            await getCurrentPageTitle(jsonData);
            break;
          case COMMAND_SEND_FILE:
            await executeSendFile(jsonData);
            break;
          case COMMAND_LOAD:
            await excuteGotoPage(jsonData);
            break;
          case COMMAND_OPEN_NEWTAB:
            await excuteGotoPageNewTab(jsonData);
            break;
          case COMMAND_CHANGE_TAB:
            await changeTab(jsonData);
            break;
          case COMMAND_CLOSE_TAB:
            await closeTab(jsonData);
            break;
          case CLEAR_CACHE:
            await clearCache(jsonData);
            break;
          case COMMAND_CAPTURE:
            await excuteCapture(jsonData);
            break;
          case COMMAND_WAIT:
            await waitPageLoad(jsonData);
            break;
          case COMMAND_SAVECOOKIE:
            await saveCookie(jsonData);
            break;
          case COMMAND_HIDE:
            await hide(jsonData);
            break;
          case COMMAND_STOP:
            await endGologin(jsonData);
            break;
          case COMMAND_RUNJS:
            await runJS(jsonData);
            break
          case COMMAND_GETJS:
            await runJS(jsonData, true);
            break;
          case COMMAND_COUNT:
            await countElement(jsonData);
            break;
          case COMMAND_WAITELEMENTVISIBLE:
            await waitForElementByXpath(jsonData, { visible: true });
            break;
          case COMMAND_WAITELEMENTDISAPPEAR:
            await waitForElementByXpath(jsonData, { hidden: true });
            break;
        }
      }
    } catch (e) {
      console.log('excuteByCommand', e);
      reponseToServer({
        code: id,
        message: RESULT_FAIL,
        error: e.message,
      });
    }
  };

  const onReceiveData = async (data) => {
    const proData = data.substr(2, data.lenght);
    const messageJson = await normalizeData(proData);
    console.log('onReceiveData ', proData);
    await excuteByCommand(messageJson);

  };

  createConnection = () => {
    //if(!global.client || )
  };

  // Connect to a server @ port 9898
  global.client = net.createConnection(
    {
      port: 8001,
      host,
    },
    () => {
      console.log('CLIENT: I connected to the server.');
      createGologinInstance();

    },
  );

  global.client.on('error', async (err) => {
    console.log('on socket error', err);
    if (global.GL) {
      await global.browser.close();
      await global.GL.stop();
      await global.GL.stopLocal({ posting: false });
      await global.client.destroy();
    }
    reponseToServer({
      code: 1,
      message: RESULT_FAIL,
      error: err.message,
    });
  });

  global.client.on('data', (data) => {
    onReceiveData(data.toString());
  });

  global.client.on('end', async () => {
    if (global.GL) {
      await global.browser.close();
      await global.GL.stop();
      await global.GL.stopLocal({ posting: false });
      await global.client.destroy();
    }
    console.log('CLIENT: I disconnected from the server.', global.client);
  });

  //     // const [page] = await global.browser.pages();
  //   const page = await global.browser.newPage();

  //   /**
  //    * Kiểm tra button SignIn + click để chuyển hướng tới trang đăng nhập
  //    */
  //   const gotoSignIn = async ()=>{
  //     const arrLoginBtn = await global.page.$x("//a[contains(@href,'https://accounts.google.com/ServiceLogin')]");
  //     if(arrLoginBtn.length){
  //         console.log('chưa login');
  //         await global.page.waitForTimeout(1000);
  //         await arrLoginBtn[0].click();
  //     }
  //   }

  //   /**
  //    * Xử lý tự động đăng nhập
  //    */

  //   const doAutomateSignIn = async ()=>{
  //     try{
  //         await global.page.waitForSelector('#identifierId')
  //         // need login
  //         await global.page.type('#identifierId',email,{ delay: 100 });
  //         await global.page.click('#identifierNext');
  //         await global.page.waitForSelector('#password input[type="password')
  //         console.log('findout password',)
  //         await global.page.waitForTimeout(500);
  //         await global.page.click('input[type="password"]')
  //         await global.page.type('input[type="password"]', password,{ delay: 150 });
  //         await global.page.waitForTimeout(500);
  //         await global.page.click('#passwordNext');
  //         await global.page.waitForTimeout(2000);
  //         await global.page.waitForNavigation();
  //         await processPageByCurrentUrl();
  //     }catch(e){
  //         //time out....
  //     }
  //   }

  //   const doVerifyAccount = async ()=>{
  //     const arrVerify = await global.page.$x("//div[@role='presentation']");
  //     if(arrVerify.length){
  //         const emailVerifyEls = await global.page.$x("//div[@role='presentation']//div[@data-challengetype='12']");
  //         if(emailVerifyEls.length){
  //             //verify email recovery
  //             //click verify recovery email
  //             emailVerifyEls[0].click();
  //             await global.page.waitForSelector("#knowledge-preregistered-email-response")
  //             await global.page.type("#knowledge-preregistered-email-response",emailRecovery);
  //             await global.page.waitForTimeout(2000);
  //             nextBtnXpath
  //         }
  //     }
  //   }

  //   /**
  //    * Kiểm tra page hiện tại, chuyển hướng xử lý tương ứng
  //    */
  //   const processPageByCurrentUrl = async()=>{
  //     const currentUrl = await global.page.url();
  //     console.log('Page loaded!',currentUrl)
  //     if(currentUrl.includes("identifier")){
  //         //check trước vì trong link đăng nhập có link redirect youtube
  //         // đang ở trang đăng nhập
  //         console.log('đang ở trang sign-in')
  //         doAutomateSignIn()
  //     }else if(currentUrl.includes("youtube.com")){
  //         //đang ở trang youtube
  //         console.log('đang ở trang youtube');
  //         gotoSignIn()
  //     }else if(currentUrl.includes("v2/challenge/")){
  //         //verify
  //         doVerifyAccount()
  //     }else if(currentUrl.includes("accounts.google.com/info/sessionexpired")){
  //         //sessionexpired

  //     }
  //     else{
  //         console.log('trang tào lao')
  //     }
  //   }

  //   /**
  //    * Đăng ký sự kiện page load
  //    */
  //   page.on('load',()=>{
  //     processPageByCurrentUrl();
  //   })

  //   await global.page.goto('https://www.youtube.com/');
  // chờ load xong trang youtube
  //   await global.page.waitForNavigation({ waitUntil: 'load' })
  //   await processPageByCurrentUrl();

  //
  //   pages[0].close();
  //   await global.page.goto('https://accounts.google.com/signin/v2/identifier');
  // try{
  //     // await global.page.waitForSelector('#identifierId')
  //     // // need login
  //     // await global.page.type('#identifierId', 'djsova123',{ delay: 100 });
  //     // await global.page.click('#identifierNext');
  //     // await global.page.waitForSelector('#password input[type="password')
  //     // console.log('findout password',)
  //     // await global.page.waitFor(500);
  //     // await global.page.click('input[type="password"]')
  //     // await global.page.type('input[type="password"]', 'Vt%ECNwIB0',{ delay: 150 });
  //     // await global.page.waitFor(500);
  //     // await global.page.click('#passwordNext');
  //     // if( await global.page.evaluate((e)=>document.querySelector('#password input[type="password"]')) ){}
  // }catch(e){
  //     //time out....

  // }
})();