const minimist = require('minimist');
const puppeteer = require('puppeteer-core');
const GoLogin = require('gologin');
const net = require('net');
// const pluginStealth =require('puppeteer-extra-plugin-stealth') ;
// puppeteer.use(pluginStealth());

let args = minimist(process.argv.slice(2));
console.log(args);

if (!args.t || !args.p  || !args.n || !args.s || !args.f || !args.e ) {
  console.log('Không tìm thấy Token, ProfileID, ipHost, Name,size');
  console.log('-t : token');
  console.log('-p : profile id');
  console.log('-n : Account Name');
  console.log('-x : proxy');
  return;
}

// 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI2MGExZjZlZDJhMWRmOGU1ZTc1NzhmMTkiLCJ0eXBlIjoiZGV2Iiwiand0aWQiOiI2MGExZjdiYzAyOWUxMGVmOWE4OTYyNzEifQ.Xu3LieGk0-LRTKzdfUsJ_v2vaynrCqLb2MaK2WL2xsQ';
// '60b9df2faa0d3ab03a0dcf74'
const proxy = args.x;
const token = args.t.trim();
const profile_id = args.p.trim();
const profileFolder=args.f.trim();

const nextBtnXpath =
  "//button[child::span[contains(text(),'Tiếp theo') or contains(text(),'Next') or contains(text(),'다음') or contains(text(),'下一步') or contains(text(),'Напред') ]]";


(async () => {
  reponseToServer = async (response) => {
    console.log('reponseToServer', response);
    global.client.write(`${JSON.stringify(response)} \n`);
  };

  createGologinInstance = async () => {
    console.log('createGologinInstance');
    try {
     let options = {
        token,
        profile_id,
        canvasMode:args.c.trim() || 'off',
        executablePath: args.e.trim(),
        tmpdir: profileFolder,
        myWindoSize:args.s.trim()
      }
           if(proxy){
       const proxyType= proxy.split(":").length
          console.log("xxxxxxxxxxxxx "+proxyType)
       if(proxyType===3){

      options={
              ...options,
                proxy:{
                  mode:proxy.split(":")[0],
                  host:proxy.split(":")[1],
                  port:proxy.split(":")[2]
              }
            }
       }else{
            options={
              ...options,
                proxy:{
                  mode:proxy.split(":")[0],
                  host:proxy.split(":")[1],
                  port:proxy.split(":")[2],
                  username:proxy.split(":")[3],
                  password:proxy.split(":")[4]
              }
            }
       }
        
        }
      global.GL = new GoLogin(options);

 
      //   const wsUrl = await GL.startLocal();
      const {status, wsUrl} =  await global.GL.startLocal();
      global.browser = await puppeteer.connect({
            browserWSEndpoint: wsUrl.toString(), 
            ignoreHTTPSErrors: true,
        });

      const pages = await global.browser.pages();
      //get last page
      let page = pages.pop();
      global.page = page;
      //close other pages
      console.log("pages "+pages.length)


      await pages.map((p) => p.close());
      const viewPort = await GL.getViewPort();
      await global.page.setViewport({ width: parseInt(args.s.split('x')[0], 10), height: parseInt(args.s.split('x')[1], 10)});
      //await global.page.setViewport({ width:1920, height:1080});
      //const pagesc = await global.browser.pages();
      //await pagesc.map((p) => p.close());
    } catch (e) {
      console.log('createGologinInstance failed', e);
    } 
  };
   createGologinInstance();
})();