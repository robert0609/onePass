import{l as u}from"./logo.90febaaa.js";import{l as d}from"./index.613def92.js";import{d as i,u as m,V as p,c as e,Z as n,a as f,Y as g,F as x}from"./vendor.bb182ad9.js";var C=i({name:"Login",setup(){const l=m(),s=p(),{token:t,handleLogin:r}=d();function a(){r().then(()=>{s.query.src_url?l.replace(decodeURIComponent(s.query.src_url)):l.replace("/home")}).catch(o=>{x.error(o.message)})}function c(o){o.key==="Enter"&&a()}return()=>e("div",{class:"flex justify-center items-center h-screen",onKeyup:c},[e("div",{class:"p-10 rounded-md shadow-md border-2"},[e("p",{class:"flex gap-2 justify-center items-center mb-2"},[e(n("ElImage"),{src:u,class:"w-9 h-9"},null),e(n("pan"),{class:"font-mono text-4xl italic font-bold text-center"},{default:()=>[f("OnePass")]})]),e("p",{class:"flex gap-2"},[e(n("ElInput"),g({placeholder:"\u8BF7\u8F93\u5165\u89E3\u9501\u7801",modelValue:t.value},{["onUpdate:modelValue"]:o=>t.value=o},{type:"password"}),null),e(n("ElButton"),{icon:"ElIconUnlock",circle:!0,onClick:a},null)])])])}});export{C as default};
//# sourceMappingURL=index.7de7e905.js.map
