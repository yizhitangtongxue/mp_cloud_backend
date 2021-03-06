layui.define(['table', 'jquery', 'element'], function (exports) {
    "use strict";

    var MOD_NAME = 'pearNotice',
        $ = layui.jquery,
		element = layui.element;
		
    var pearNotice = function (opt) {
        this.option = opt;
    };

    pearNotice.prototype.render = function (opt) {
        //默认配置值
		var option = {
			elem:opt.elem,
			url:opt.url,
			height:opt.height,
			data:opt.data,
			click:opt.click
		}
		
		option.data = getData(option.url);
		
		var notice = createHtml(option);
		
		$("#"+option.elem).html(notice);
		
		// 添加监听
		$("*[notice-id]").click(function(){
			var id = $(this).attr("notice-id");
			var title = $(this).attr("notice-title");
		    option.click(id,title);
		})
		
		return new pearNotice(option);
    } 
	
	/** 同 步 请 求 获 取 数 据 */
	function getData(url){
		
		$.ajaxSettings.async = false;
		var data = null;
		
		$.get(url, function(result) {
			data = result;
		});
		
		$.ajaxSettings.async = true;
		return data;
	}
	
	function createHtml(option){
		
	
		
		var notice = '<li class="layui-nav-item" lay-unselect="">'+
		    '<a href="#" class="notice layui-icon layui-icon-notice"><span class="layui-badge-dot"></span></a>'+
		    '<div class="layui-nav-child layui-tab pear-notice" style="left: -200px;">';
		
		var noticeTitle = '<ul class="layui-tab-title">';
		
		var noticeContent = '<div class="layui-tab-content" style="height:'+option.height+';overflow-x: hidden;">'
		
		var index = 0;
		
		// 根据 data 便利数据
		 $.each(option.data, function(i, item) {
			 
			 
			 
			 if(index==0){
			 
			 noticeTitle += '<li class="layui-this">'+item.title+'</li>';
			 
			 noticeContent += '<div class="layui-tab-item layui-show">';
			 
			 }else{
			
			noticeTitle += '<li>'+item.title+'</li>';
			
			noticeContent += '<div class="layui-tab-item">';
				 
			 }
			 
			 $.each(item.children, function(i, note) {
				 
				 noticeContent += '<div class="pear-notice-item" notice-title="'+note.title+'" notice-id="'+note.id+'">'+
						 '<img src="'+note.avatar+'">'+
						 '<span>'+note.title+'</span>'+
						 '<span>'+note.time+'</span>'+
					     '</div>';
				 
			 })
			 
			 noticeContent += '</div>';
			 
			 
			 index++;
		 })
		 
		 noticeTitle += '</ul>'; 
		 
		 noticeContent += '</div>';
		 
		 notice += noticeTitle;
		 
		 notice += noticeContent;
		 
		 notice += '</div></li>';
		
		 
		 return notice;
		 
	}
	
	exports(MOD_NAME,new pearNotice());
})