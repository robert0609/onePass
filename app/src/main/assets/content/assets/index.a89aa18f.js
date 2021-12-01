import{L as tt}from"./index.4961e40c.js";import{a as et,A as nt}from"./index.4f80c6b2.js";import{a1 as rt,a2 as it,d as P,a3 as R,W as H,a4 as M,c as m,Z as _,a as L,Y as F,_ as D,V as ot,f as I,G as at,F as ut}from"./vendor.bb182ad9.js";/* empty css                        */import"./logo.90febaaa.js";import"./index.613def92.js";var U={exports:{}};/*!
 * clipboard.js v2.0.8
 * https://clipboardjs.com/
 *
 * Licensed MIT © Zeno Rocha
 */(function(S,w){(function(x,g){S.exports=g()})(it,function(){return function(){var b={134:function(u,a,t){t.d(a,{default:function(){return K}});var c=t(279),s=t.n(c),i=t(370),y=t.n(i),p=t(817),E=t.n(p);function f(r){return typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?f=function(e){return typeof e}:f=function(e){return e&&typeof Symbol=="function"&&e.constructor===Symbol&&e!==Symbol.prototype?"symbol":typeof e},f(r)}function h(r,n){if(!(r instanceof n))throw new TypeError("Cannot call a class as a function")}function d(r,n){for(var e=0;e<n.length;e++){var o=n[e];o.enumerable=o.enumerable||!1,o.configurable=!0,"value"in o&&(o.writable=!0),Object.defineProperty(r,o.key,o)}}function k(r,n,e){return n&&d(r.prototype,n),e&&d(r,e),r}var B=function(){function r(n){h(this,r),this.resolveOptions(n),this.initSelection()}return k(r,[{key:"resolveOptions",value:function(){var e=arguments.length>0&&arguments[0]!==void 0?arguments[0]:{};this.action=e.action,this.container=e.container,this.emitter=e.emitter,this.target=e.target,this.text=e.text,this.trigger=e.trigger,this.selectedText=""}},{key:"initSelection",value:function(){this.text?this.selectFake():this.target&&this.selectTarget()}},{key:"createFakeElement",value:function(){var e=document.documentElement.getAttribute("dir")==="rtl";this.fakeElem=document.createElement("textarea"),this.fakeElem.style.fontSize="12pt",this.fakeElem.style.border="0",this.fakeElem.style.padding="0",this.fakeElem.style.margin="0",this.fakeElem.style.position="absolute",this.fakeElem.style[e?"right":"left"]="-9999px";var o=window.pageYOffset||document.documentElement.scrollTop;return this.fakeElem.style.top="".concat(o,"px"),this.fakeElem.setAttribute("readonly",""),this.fakeElem.value=this.text,this.fakeElem}},{key:"selectFake",value:function(){var e=this,o=this.createFakeElement();this.fakeHandlerCallback=function(){return e.removeFake()},this.fakeHandler=this.container.addEventListener("click",this.fakeHandlerCallback)||!0,this.container.appendChild(o),this.selectedText=E()(o),this.copyText(),this.removeFake()}},{key:"removeFake",value:function(){this.fakeHandler&&(this.container.removeEventListener("click",this.fakeHandlerCallback),this.fakeHandler=null,this.fakeHandlerCallback=null),this.fakeElem&&(this.container.removeChild(this.fakeElem),this.fakeElem=null)}},{key:"selectTarget",value:function(){this.selectedText=E()(this.target),this.copyText()}},{key:"copyText",value:function(){var e;try{e=document.execCommand(this.action)}catch{e=!1}this.handleResult(e)}},{key:"handleResult",value:function(e){this.emitter.emit(e?"success":"error",{action:this.action,text:this.selectedText,trigger:this.trigger,clearSelection:this.clearSelection.bind(this)})}},{key:"clearSelection",value:function(){this.trigger&&this.trigger.focus(),document.activeElement.blur(),window.getSelection().removeAllRanges()}},{key:"destroy",value:function(){this.removeFake()}},{key:"action",set:function(){var e=arguments.length>0&&arguments[0]!==void 0?arguments[0]:"copy";if(this._action=e,this._action!=="copy"&&this._action!=="cut")throw new Error('Invalid "action" value, use either "copy" or "cut"')},get:function(){return this._action}},{key:"target",set:function(e){if(e!==void 0)if(e&&f(e)==="object"&&e.nodeType===1){if(this.action==="copy"&&e.hasAttribute("disabled"))throw new Error('Invalid "target" attribute. Please use "readonly" instead of "disabled" attribute');if(this.action==="cut"&&(e.hasAttribute("readonly")||e.hasAttribute("disabled")))throw new Error(`Invalid "target" attribute. You can't cut text from elements with "readonly" or "disabled" attributes`);this._target=e}else throw new Error('Invalid "target" value, use a valid Element')},get:function(){return this._target}}]),r}(),z=B;function T(r){return typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?T=function(e){return typeof e}:T=function(e){return e&&typeof Symbol=="function"&&e.constructor===Symbol&&e!==Symbol.prototype?"symbol":typeof e},T(r)}function Y(r,n){if(!(r instanceof n))throw new TypeError("Cannot call a class as a function")}function N(r,n){for(var e=0;e<n.length;e++){var o=n[e];o.enumerable=o.enumerable||!1,o.configurable=!0,"value"in o&&(o.writable=!0),Object.defineProperty(r,o.key,o)}}function $(r,n,e){return n&&N(r.prototype,n),e&&N(r,e),r}function G(r,n){if(typeof n!="function"&&n!==null)throw new TypeError("Super expression must either be null or a function");r.prototype=Object.create(n&&n.prototype,{constructor:{value:r,writable:!0,configurable:!0}}),n&&O(r,n)}function O(r,n){return O=Object.setPrototypeOf||function(o,l){return o.__proto__=l,o},O(r,n)}function q(r){var n=X();return function(){var o=C(r),l;if(n){var v=C(this).constructor;l=Reflect.construct(o,arguments,v)}else l=o.apply(this,arguments);return J(this,l)}}function J(r,n){return n&&(T(n)==="object"||typeof n=="function")?n:W(r)}function W(r){if(r===void 0)throw new ReferenceError("this hasn't been initialised - super() hasn't been called");return r}function X(){if(typeof Reflect=="undefined"||!Reflect.construct||Reflect.construct.sham)return!1;if(typeof Proxy=="function")return!0;try{return Date.prototype.toString.call(Reflect.construct(Date,[],function(){})),!0}catch{return!1}}function C(r){return C=Object.setPrototypeOf?Object.getPrototypeOf:function(e){return e.__proto__||Object.getPrototypeOf(e)},C(r)}function j(r,n){var e="data-clipboard-".concat(r);if(!!n.hasAttribute(e))return n.getAttribute(e)}var Z=function(r){G(e,r);var n=q(e);function e(o,l){var v;return Y(this,e),v=n.call(this),v.resolveOptions(l),v.listenClick(o),v}return $(e,[{key:"resolveOptions",value:function(){var l=arguments.length>0&&arguments[0]!==void 0?arguments[0]:{};this.action=typeof l.action=="function"?l.action:this.defaultAction,this.target=typeof l.target=="function"?l.target:this.defaultTarget,this.text=typeof l.text=="function"?l.text:this.defaultText,this.container=T(l.container)==="object"?l.container:document.body}},{key:"listenClick",value:function(l){var v=this;this.listener=y()(l,"click",function(A){return v.onClick(A)})}},{key:"onClick",value:function(l){var v=l.delegateTarget||l.currentTarget;this.clipboardAction&&(this.clipboardAction=null),this.clipboardAction=new z({action:this.action(v),target:this.target(v),text:this.text(v),container:this.container,trigger:v,emitter:this})}},{key:"defaultAction",value:function(l){return j("action",l)}},{key:"defaultTarget",value:function(l){var v=j("target",l);if(v)return document.querySelector(v)}},{key:"defaultText",value:function(l){return j("text",l)}},{key:"destroy",value:function(){this.listener.destroy(),this.clipboardAction&&(this.clipboardAction.destroy(),this.clipboardAction=null)}}],[{key:"isSupported",value:function(){var l=arguments.length>0&&arguments[0]!==void 0?arguments[0]:["copy","cut"],v=typeof l=="string"?[l]:l,A=!!document.queryCommandSupported;return v.forEach(function(Q){A=A&&!!document.queryCommandSupported(Q)}),A}}]),e}(s()),K=Z},828:function(u){var a=9;if(typeof Element!="undefined"&&!Element.prototype.matches){var t=Element.prototype;t.matches=t.matchesSelector||t.mozMatchesSelector||t.msMatchesSelector||t.oMatchesSelector||t.webkitMatchesSelector}function c(s,i){for(;s&&s.nodeType!==a;){if(typeof s.matches=="function"&&s.matches(i))return s;s=s.parentNode}}u.exports=c},438:function(u,a,t){var c=t(828);function s(p,E,f,h,d){var k=y.apply(this,arguments);return p.addEventListener(f,k,d),{destroy:function(){p.removeEventListener(f,k,d)}}}function i(p,E,f,h,d){return typeof p.addEventListener=="function"?s.apply(null,arguments):typeof f=="function"?s.bind(null,document).apply(null,arguments):(typeof p=="string"&&(p=document.querySelectorAll(p)),Array.prototype.map.call(p,function(k){return s(k,E,f,h,d)}))}function y(p,E,f,h){return function(d){d.delegateTarget=c(d.target,E),d.delegateTarget&&h.call(p,d)}}u.exports=i},879:function(u,a){a.node=function(t){return t!==void 0&&t instanceof HTMLElement&&t.nodeType===1},a.nodeList=function(t){var c=Object.prototype.toString.call(t);return t!==void 0&&(c==="[object NodeList]"||c==="[object HTMLCollection]")&&"length"in t&&(t.length===0||a.node(t[0]))},a.string=function(t){return typeof t=="string"||t instanceof String},a.fn=function(t){var c=Object.prototype.toString.call(t);return c==="[object Function]"}},370:function(u,a,t){var c=t(879),s=t(438);function i(f,h,d){if(!f&&!h&&!d)throw new Error("Missing required arguments");if(!c.string(h))throw new TypeError("Second argument must be a String");if(!c.fn(d))throw new TypeError("Third argument must be a Function");if(c.node(f))return y(f,h,d);if(c.nodeList(f))return p(f,h,d);if(c.string(f))return E(f,h,d);throw new TypeError("First argument must be a String, HTMLElement, HTMLCollection, or NodeList")}function y(f,h,d){return f.addEventListener(h,d),{destroy:function(){f.removeEventListener(h,d)}}}function p(f,h,d){return Array.prototype.forEach.call(f,function(k){k.addEventListener(h,d)}),{destroy:function(){Array.prototype.forEach.call(f,function(k){k.removeEventListener(h,d)})}}}function E(f,h,d){return s(document.body,f,h,d)}u.exports=i},817:function(u){function a(t){var c;if(t.nodeName==="SELECT")t.focus(),c=t.value;else if(t.nodeName==="INPUT"||t.nodeName==="TEXTAREA"){var s=t.hasAttribute("readonly");s||t.setAttribute("readonly",""),t.select(),t.setSelectionRange(0,t.value.length),s||t.removeAttribute("readonly"),c=t.value}else{t.hasAttribute("contenteditable")&&t.focus();var i=window.getSelection(),y=document.createRange();y.selectNodeContents(t),i.removeAllRanges(),i.addRange(y),c=i.toString()}return c}u.exports=a},279:function(u){function a(){}a.prototype={on:function(t,c,s){var i=this.e||(this.e={});return(i[t]||(i[t]=[])).push({fn:c,ctx:s}),this},once:function(t,c,s){var i=this;function y(){i.off(t,y),c.apply(s,arguments)}return y._=c,this.on(t,y,s)},emit:function(t){var c=[].slice.call(arguments,1),s=((this.e||(this.e={}))[t]||[]).slice(),i=0,y=s.length;for(i;i<y;i++)s[i].fn.apply(s[i].ctx,c);return this},off:function(t,c){var s=this.e||(this.e={}),i=s[t],y=[];if(i&&c)for(var p=0,E=i.length;p<E;p++)i[p].fn!==c&&i[p].fn._!==c&&y.push(i[p]);return y.length?s[t]=y:delete s[t],this}},u.exports=a,u.exports.TinyEmitter=a}},x={};function g(u){if(x[u])return x[u].exports;var a=x[u]={exports:{}};return b[u](a,a.exports,g),a.exports}return function(){g.n=function(u){var a=u&&u.__esModule?function(){return u.default}:function(){return u};return g.d(a,{a}),a}}(),function(){g.d=function(u,a){for(var t in a)g.o(a,t)&&!g.o(u,t)&&Object.defineProperty(u,t,{enumerable:!0,get:a[t]})}}(),function(){g.o=function(u,a){return Object.prototype.hasOwnProperty.call(u,a)}}(),g(134)}().default})})(U);var V=rt(U.exports),ct=P({name:"AccountCard",props:{account:{type:Object,required:!0}},emits:["clickEdit"],setup(S,{emit:w}){const b=S.account,x=R(()=>`copy-username-${b.id}`),g=R(()=>`copy-password-${b.id}`);return H(()=>{new V(`#${x.value}`,{text:()=>b.userName}).on("success",t=>{M({message:"\u590D\u5236\u7528\u6237\u540D\u6210\u529F",type:"success"}),t.clearSelection()}),new V(`#${g.value}`,{text:()=>b.password}).on("success",t=>{M({message:"\u590D\u5236\u5BC6\u7801\u6210\u529F",type:"success"}),t.clearSelection()})}),()=>m(D,null,[m(_("ElCard"),{shadow:"hover",class:" w-1/5"},{default:()=>[m("div",{class:"flex"},[b.userName]),m("div",{class:"flex justify-end"},[m(_("ElLink"),{type:"primary",href:"javascript:void(0);",onClick:()=>w("clickEdit",b)},{default:()=>[L("\u7F16\u8F91")]}),m(_("ElDivider"),{direction:"vertical"},null),m(_("ElLink"),F({id:x.value},{type:"primary",href:"javascript:void(0);"}),{default:()=>[L("\u590D\u5236\u7528\u6237\u540D")]}),m(_("ElDivider"),{direction:"vertical"},null),m(_("ElLink"),F({id:g.value},{type:"primary",href:"javascript:void(0);"}),{default:()=>[L("\u590D\u5236\u5BC6\u7801")]})])]})])}}),yt=P({name:"AccountList",setup(){const S=ot();let w;const{siteId:b,accountList:x,fetchAccountList:g}=et(),u=I(!1),a=I(0);H(()=>{b.value=Number(S.query.site_id),t()});function t(){w=at.service({target:"#account-list"}),g().catch(i=>{ut.error(i.message)}).finally(()=>{w&&w.close()})}function c(i){i?a.value=i.id:a.value=0,u.value=!0}function s(){u.value=!1,t()}return()=>m(D,null,[m(tt,null,{default:()=>[m("div",{class:"flex justify-end"},[m(_("ElButton"),{icon:"ElIconPlus",type:"primary",onClick:()=>c()},{default:()=>[L("\u65B0\u589E\u8D26\u53F7")]})]),m("div",{id:"account-list",class:"flex gap-4 flex-wrap"},[x.value.map(i=>m(ct,{account:i,onClickEdit:c},null))])]}),m(_("ElDrawer"),F({title:"\u8D26\u53F7\u7F16\u8F91",size:"30%"},{modelValue:u.value,["onUpdate:modelValue"]:i=>u.value=i}),{default:()=>[u.value?m(nt,{id:a.value,siteId:b.value,onConfirm:s,onCancel:()=>u.value=!1},null):void 0]})])}});export{yt as default};
//# sourceMappingURL=index.a89aa18f.js.map