import{U as o}from"./index.c1784500.js";import{c as a,Y as i,X as g,Z as c}from"./vendor.3eeb42f9.js";const s="_table__header_9uj5n_1",h="_table__header__cell_9uj5n_5",f="_pagination_9uj5n_10";var t={"el-table":"_el-table_9uj5n_1",table__header:s,table__header__cell:h,pagination:f};const l=function(e,{attrs:u,slots:r,emit:d}){if(e.showPagination===void 0&&(e.showPagination=!0),e.showPagination&&(e.pageSize===void 0||e.currentPageIndex===void 0||e.totalCount===void 0))throw o.createUIError("\u8868\u683C\u7EC4\u4EF6\u663E\u793A\u5206\u9875\u5668\u7684\u65F6\u5019\uFF0C\u5FC5\u987B\u8981\u4F20\u8F93pageSize\u3001currentPageIndex\u3001totalCount");function _(n){d("pageIndexChanged",n)}return a(c,null,[a(i("ElTable"),Object.assign({class:t["el-table"],stripe:!0,size:"small",["empty-text"]:"\u6682\u65E0\u6570\u636E",["header-row-class-name"]:t.table__header,["header-cell-class-name"]:t.table__header__cell},u),{default:()=>[r.default?r.default():void 0]}),e.showPagination?a("p",null,[a(i("ElPagination"),g({class:"mt-4 flex justify-center",layout:"prev, pager, next",background:!0,pageSize:e.pageSize,total:e.totalCount},{currentPage:e.currentPageIndex,["onUpdate:currentPage"]:n=>{d("update:currentPageIndex",n-1)},onCurrentChange:_,hideOnSinglePage:!0}),null)]):void 0])};l.displayName="OneTable";l.emits=["pageIndexChanged","update:currentPageIndex"];var x=l;export{x as O};
//# sourceMappingURL=index.7ecd3942.js.map
