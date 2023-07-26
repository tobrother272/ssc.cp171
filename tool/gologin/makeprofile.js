const puppeteer = require('puppeteer-core');
const GoLogin = require('gologin');
const minimist = require('minimist');
const delay = ms => new Promise(res => setTimeout(res, ms));
let args = minimist(process.argv.slice(2));
console.log(args);

if (!args.p || !args.t ||!args.r ||!args.u||!args.n) {
  console.log('Không tìm thấy os,token,resolution,userAgent');
  return;
}

(async () =>{
    const GL = new GoLogin({
        token: args.t.trim(),
    });
    
    // next parameters are required for creating

    const profile_id = await GL.create({
        name: args.n.trim(),
        os: args.p.trim(),
        navigator: {
            language: 'enUS',
            resolution:args.r.trim(),
            platform: args.p.trim(),
            userAgent:args.u.trim()
        }
    });

    console.log('profile id=', profile_id);


    //await GL.delete(profile_id);
})();