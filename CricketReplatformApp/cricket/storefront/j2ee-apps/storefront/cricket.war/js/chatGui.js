function chatGui (conf, chatConf) {
    this.chatStartRequested = false;
    this.startOnFirstLine = false;
    this.startLine = "";
    this.ver = '0.5';
    this.conf = conf;
    this.chatConf = chatConf;
    this.randomNum = Math.round(1000*Math.random());
    //this.cssTemplate = "<style> .chatboxDIV { display: none;width: 220px; height: 250px; border: 3px solid #CFE7FF; } .chatboxhead { padding: 2px; font-family: Verdana; font-size: 12px; background:#CFE7FF; height: 20px; font-weight: bold; } .chatboxcontent { overflow: auto; padding: 2px; font-family: Verdana; font-size: 10px; height: 166px; border: 1px solid #B5C3D0; } .inputFocus { border: 2px solid #3574FF; } .inputNoFocus { border: 1px solid #B5C3D0; } .chatboxtextarea { font-family: Verdana; font-size: 10px; height:50px; margin:1px; overflow:hidden; width: 216px; } .chatboxmessagefrom { font-family: Verdana; font-size: 10px; font-weight: bold; } .chatboxmessagecontent { font-family: Verdana; font-size: 10px; overflow: auto; } </style>";
    this.cssTemplate = '';
    this.htmlTemplate = "<div id='chatboxcontent-{RANDOM}' class='chatboxcontent ui-corner-all'> \
        </div> \
        <div class='chatboxtextareaDiv'>\
            <textarea id='chatboxtextarea-{RANDOM}' class='chatboxtextarea ui-corner-all inputFocus'></textarea> \
        </div>\
    </div>";
    this.lpc = null;
    this.chatCookie = 'CHATCOOKIE-'+ chatConf.lpNumber;
    this.baseTitle = 'Chat';
}

chatGui.prototype.hideDiv = function () {
    $('#'+this.conf.divID).hide('slow');    
};

chatGui.prototype.adjustContentHeight = function (height) {
    if (typeof(height)=='undefined') {
        height = $('#'+this.conf.divID).height() - 77;
    }
    else {
        height = height - 77;
    }
    if (height<20) { height=20;}
    $('.chatboxcontent').height(height);
};

chatGui.prototype.populateDiv = function (resume) {
    if (typeof(resume)=='undefined') { resume = false; }
    
    var html = this.cssTemplate + this.htmlTemplate.replace(/\{RANDOM\}/g, this.randomNum);

    $('#'+this.conf.divID).html(html);
    if (resume && this.conf.rememberPosition) {
        var r = this.parseCookie();
        if (r.height) {
            $('#'+this.conf.divID).css({
                height : r.height + 'px',
                width : r.width + 'px'
            });
        }
        else {
            $('#'+this.conf.divID).height(this.conf.height);
            $('#'+this.conf.divID).width(this.conf.width);   
        }
        if (r.left) {
            $('#'+this.conf.divID).css({
                position : 'absolute',
                left : r.left + 'px',
                top : r.top + 'px'
            });
        }
        this.adjustContentHeight(r.height);

    }
    else {
	    
        $('#'+this.conf.divID).height(this.conf.height);
        $('#'+this.conf.divID).width(this.conf.width);
        this.adjustContentHeight();
		 $('#'+this.conf.divID).show('slow'); 
    }

    var that = this;
    
    if (this.conf.resizable) {
        $('#'+this.conf.divID).resizable({
                                            resize: function(event, ui) {
                                                that.myOnResize();
                                            }
                                            , stop : function (event, ui) {
                                                that.myOnResizeStop();
                                            }
                                        });
    }    
    $('#chatbox-'+ this.randomNum).show('slow');
};

chatGui.prototype.init = function () {
    if (this.conf.autoStart || this.isResumeChat()) {
       this.startChat();
    }    
};

chatGui.prototype.initUIChat = function () {
    this.startOnFirstLine = true;
    //copy over the object ref
	this.lpc = window.lpc;    
    var resume = this.isResumeChat();
    this.populateDiv(resume);
    this.attachEvents();
};

chatGui.prototype.startChat = function () {
	this.commenceChat();
	//setTimeout( function() {  window.chatG.commenceChat() }, 3000);
}

chatGui.prototype.commenceChat = function () {
    var that = this;
    //copy over the object ref
	this.lpc = window.lpc;    
    if(typeof(lpChat)!='undefined'){
		if (typeof(window.lpChatConfig)=='undefined') { window.lpChatConfig = {};}
        
        this.lpc.onInit = $(function () {that.myInitHandler();});
        this.lpc.onStart = $(function (agentID,agentName) {that.myOnStart(agentID,agentName);});
        this.lpc.onAvailability = $(function (availObj) {that.myOnAvailability(availObj);});  
        this.lpc.onStop = $(function (reasonID,reasonText) {that.myStopChat(reasonID,reasonText);});
        this.lpc.onState = $(function (stateID) {that.myOnState(stateID);});
        this.lpc.onAgentTyping = $(function (on) {that.myTypingHandler(on);});
        this.lpc.onUrlPush = $(function (url) {that.myPushHandler(url);});
        this.lpc.onLine = $(function (lineObj) {that.myLineHandler(lineObj);});
        this.lpc.onError = $(function (errorobj) {that.myErrorHandler(errorobj);});
        
        for (var name in this.chatConf) {
            this.lpc[name] = this.chatConf[name];
        }
    }
    else {
		alert('lpChat undefined');
        throw {message: "lpChat undefined"};
    }

    var resume = this.isResumeChat();
    this.populateDiv(resume);
    this.attachEvents();
    if (resume) {
        var r = this.parseCookie();
        this.lpc.sessionkey = r.sessionkey;
        this.lpc.startChatSequence();
        that.scrollToBottom();
    }
    else {
        this.lpc.requestChat();
    }
};

chatGui.prototype.myOnAvailability = function(availObj){  
	if( availObj.availability == true ){  
		alert( 'we can start a chat' );  
	} else {  
		this.addLineFromSystem( 'account is offline' );  
	}  
}  

chatGui.prototype.isResumeChat = function (){
    var r = this.parseCookie();
    this.log('isResumeChat:'+(r.sessionkey?true:false));
    return (r.sessionkey?true:false);
};

chatGui.prototype.scrollToBottom = function (){
    var objDiv = $('#chatboxcontent-'+this.randomNum);
    if (objDiv) {
    	objDiv.scrollTop(objDiv[0].scrollHeight);
    }
};

chatGui.prototype.attachEvents = function (){
    var that = this;
    $('#chatboxtextarea-'+this.randomNum).bind('focus', function (event) {
        $(this).removeClass('inputNoFocus');
        $(this).addClass('inputFocus');
    });
    $('#chatboxtextarea-'+this.randomNum).bind('blur', function (event) {
   //     $(this).removeClass('inputFocus');
   //     $(this).addClass('inputNoFocus');
    });
    $('#chatboxtextarea-'+this.randomNum).bind('keydown', function (event) {
        if (that.startOnFirstLine && event.keyCode == 13 && event.shiftKey == 0 && that.lpc.getState().id!=2 && !that.chatStartRequested) { //Enter without chat
	  	    that.chatStartRequested = true;
	        that.startChat();
	 //     that.addLine($(this).val());
	 	    
	 	    that.startLine = $(this).val();
	        $(this).val('');
	        return false;
        }

        if (event.keyCode == 13 && event.shiftKey == 0 && that.lpc.getState().id==2) { //Enter
            that.addLine($(this).val());
            $(this).val('');
            return false;
        }
        if (that.lpc.getState().id!=2 && (that.chatStartRequested || !that.startOnFirstLine)) {
        	if (event.keyCode == 13 && event.shiftKey == 0) { //Enter
            that.addLine($(this).val());
            $(this).val('');
            return false;
        }
            //return false;
        }
        
		return true;
		
    });
    
    $('#chatboxcontent-'+this.randomNum).bind('click', function (event) {
        $('#chatboxtextarea-'+that.randomNum).focus();
    });
    
    $('#chatBoxCloseChat-'+this.randomNum).bind('click', function (event) {
        that.lpc.endChat();
        that.hideDiv();
        that.deleteCookie( that.chatCookie);
    });
};

chatGui.prototype.addLineFromSystem = function (text, doLine){
    if (this.conf.maskVisitorDataRegExp) {
        var exp = new RegExp(this.conf.maskVisitorDataRegExp);
        text = text.replace(exp,this.conf.maskVisitorDataReplace);
    }
    if (!doLine && this.conf.enableGoogleTranslate) {
        var that = this;
        google.language.translate(text, this.conf.googleTranslateVisitorTargetLanguage, this.conf.googleTranslateAgentTargetLanguage, function(result) {
            var trans = '';
            if (!result.error) {
                if (result.detectedSourceLanguage != that.conf.googleTranslateAgentTargetLanguage) {
                    var sendText = text;
                    if (text != result.translation) {
                        trans = '('+ that.conf.googleTranslateAgentTargetLanguage + ': ' + result.translation + ')';
                    }
                }
            }
            sendText += ' ' + trans;

            that.myLineHandler({
                by: 'LivePerson System'//that.lpc.getVisitorName()
                ,text : text
                ,translation : trans
                ,type : that.lpc.LineType.SYSTEM
            });
            that.lpc.addLine(sendText);
        });
    }
    else {
        this.myLineHandler({
            by: 'LivePerson System',//that.lpc.getVisitorName(),
            text : text,
            type : this.lpc.LineType.SYSTEM
        });
        this.lpc.addLine(text);
    }
};

chatGui.prototype.addLine = function (text, doLine){
    if (this.conf.maskVisitorDataRegExp) {
        var exp = new RegExp(this.conf.maskVisitorDataRegExp);
        text = text.replace(exp,this.conf.maskVisitorDataReplace);
    }
    if (!doLine && this.conf.enableGoogleTranslate) {
        var that = this;
        google.language.translate(text, this.conf.googleTranslateVisitorTargetLanguage, this.conf.googleTranslateAgentTargetLanguage, function(result) {
            var trans = '';
            if (!result.error) {
                if (result.detectedSourceLanguage != that.conf.googleTranslateAgentTargetLanguage) {
                    var sendText = text;
                    if (text != result.translation) {
                        trans = '('+ that.conf.googleTranslateAgentTargetLanguage + ': ' + result.translation + ')';
                    }
                }
            }
            sendText += ' ' + trans;

            that.myLineHandler({
                by: that.lpc.getVisitorName()
                ,text : text
                ,translation : trans
                ,type : that.lpc.LineType.VISITOR
            });
            that.lpc.addLine(sendText);
        });
    }
    else {
        this.myLineHandler({
            by: this.lpc.getVisitorName(),
            text : text,
            type : this.lpc.LineType.VISITOR
        });
        this.lpc.addLine(text);
    }
};

chatGui.prototype.myOnDrag = function (){
    this.dragged = true;
    this.updateCookie();
    var pos = $('#'+this.conf.divID).position();
    $('#'+this.conf.divID).css({position: 'absolute', left : Math.ceil(pos.left) + 'px', top : Math.ceil(pos.top) + 'px'})
};

chatGui.prototype.myOnResizeStop = function (){
    this.updateCookie();
}
chatGui.prototype.myOnResize = function (){
    this.adjustContentHeight();
    if ($('#'+this.conf.divID).width() < 120) {
        $('#'+this.conf.divID).width(120);    
    }
    if ($('#'+this.conf.divID).height() < 120) {
        $('#'+this.conf.divID).height(120);    
    }
    this.resized = true;
};

chatGui.prototype.parseCookie = function (){
    var res = {};
    var cookie = this.getCookie(this.chatCookie);
    if (cookie !=null) {
        var data = cookie.split('|');
        for (var i=0; i < data.length; i++) {
            var temp = data[i].split('#');
            res[temp[0]] = temp[1];
        }
        if (res.height) { res.height = parseInt(res.height); }
        if (res.width) { res.width = parseInt(res.width); }
        if (res.top) { res.top = parseInt(res.top); }
        if (res.left) { res.left = parseInt(res.left); }
    }
    return res;
};

chatGui.prototype.updateCookie = function (){
    var value = '';
    if (this.conf.autoResumechat && this.lpc.sessionkey!=-1 ) {
        value += 'sessionkey#' + this.lpc.sessionkey + '|';
    }
    if (this.conf.rememberPosition) {
        if (this.conf.resizable && this.resized) {
            value += 'height#' + Math.ceil($('#'+this.conf.divID).height()) + '|';
            value += 'width#' + Math.ceil($('#'+this.conf.divID).width()) + '|';
        }
        if (this.conf.draggable && this.dragged) {
            var pos = $('#'+this.conf.divID).position();
            value += 'left#' + Math.ceil(pos.left) + '|';
            value += 'top#' +  Math.ceil(pos.top) + '|';
        }
    }
    if (value.length > 0) {
        value = value.substr(0, value.length-1);
    }
    this.setCookie( this.chatCookie, value);
};


chatGui.prototype.myInitHandler = function (){
    var img = new Image();
    img.src = 'img/smile.png';
    img.src = 'img/laugh.png';
    img.src = 'img/unhappy.png';
    if (typeof(this.conf.onInit)!='undefined') {
        this.conf.onInit();
    }
    if (typeof(this.conf.visitor)!='undefined') {
        this.lpc.setVisitorName(this.conf.visitor);
    }
};

chatGui.prototype.myOnStart = function (agentID,agentName){
    this.updateCookie();
    this.baseTitle = 'Agent ' + this.lpc.getAgentName();
    this.setTitle(this.baseTitle);
    if (typeof(this.conf.onStart)!='undefined') {
        this.conf.onStart(agentID,agentName);
    }
};

chatGui.prototype.myStopChat = function (reasonID,reasonText){
    this.deleteCookie( this.chatCookie);
    if (typeof(this.conf.onStop)!='undefined') {
        this.conf.onStop(reasonID,reasonText);
        this.log('myStopChat:'+ reasonID + ' ' + reasonText);
    }
};

chatGui.prototype.myOnState = function (stateObj){
    var stateID = stateObj.id;
    if (stateID==1) { // inSite
        this.setTitle('Chat Initializing');
    }
    else if (stateID==2) { // inChat
        this.setTitle('Chat Started');
        if (this.startOnFirstLine){
        	this.addLine(this.startLine);
        }
    }
    else if (stateID==4) { // waiting for chat
        this.setTitle('Waiting for Chat');
    }
    else if (stateID==6) { //after Chat

    }
};

chatGui.prototype.myTypingHandler = function (on){
    if (on) {
        this.setTitle(this.getTitle() + ' is Typing');
    }
    else {
        this.setTitle(this.baseTitle);
    }
};

chatGui.prototype.myPushHandler = function (url){
};

chatGui.prototype.myLineHandler = function (lineObj){

	if ($("#chatboxcontent-"+this.randomNum).length == 0) {
		setTimeout( function() {window.chatG.myLineHandler(lineObj); }, 2000);
		return;
	}
	
	for (var p in lineObj) {
	if (p == 'by')
		console.log(p + ': ' + lineObj[p]);
	}
	
	if (!lineObj.by) {
		return;
	}

    var html = '';
    var time = '';
    if (this.conf.showTime) {
        var myDate;
        if (lineObj.time) {
            myDate = new Date(parseInt(lineObj.time) * 1000);
        }
        else {
            myDate = new Date();
        }
        time = '['+ myDate.getHours() +':'+ myDate.getMinutes() +':'+ myDate.getSeconds() +'] ';
    }

    if (lineObj.type == this.lpc.LineType.VISITOR) { // visitor
        var msg = lineObj.text;
        msg = this.addEmoticons(msg);
        if (lineObj.translation) {
            msg = msg + '<span class="chatboxmessageTranslated"> ' + lineObj.translation + '</span>';
        }
        html = '<div class="chatboxmessage"><span class="chatboxmessagefromVisitor">'+ time + lineObj.by +':&nbsp;</span><span class="chatboxmessagecontentVisitor">'+ msg +'</span></div>';
    }
    else if (lineObj.type == this.lpc.LineType.AGENT) { // agent
        if (this.conf.enableGoogleTranslate) {
            var that = this;
            google.language.translate(lineObj.text, this.conf.googleTranslateAgentTargetLanguage, this.conf.googleTranslateVisitorTargetLanguage, function(result) {
                if (!result.error) {
                    if (result.detectedSourceLanguage != that.conf.googleTranslateVisitorTargetLanguage) {
                        var sendText = lineObj.text + ' <span class="chatboxmessageTranslated"> ('+ that.conf.googleTranslateVisitorTargetLanguage + ': ' + result.translation + ')</span>';
                        lineObj.text = sendText;
                        that.showLineFromAgent(lineObj, time);
                    }
                }
            });
        }
        else {
            this.showLineFromAgent(lineObj, time);
        }
        return;
    }
    else if (lineObj.type == this.lpc.LineType.SYSTEM) { // system
        html = '<div class="chatboxmessage"><span class="chatboxmessagefromSystem">'+ time + lineObj.by +':&nbsp;</span><span class="chatboxmessagecontentSystem">'+ lineObj.text +'</span></div>';
    }
    else {
        html = '<div class="chatboxmessage"><span class="chatboxmessagefrom">'+ time + lineObj.by +':&nbsp;</span><span class="chatboxmessagecontent">'+ lineObj.text +'</span></div>';
        this.log('NO TYPE');
    }
    $("#chatboxcontent-"+this.randomNum).append(html);
    this.scrollToBottom();
};



chatGui.prototype.showLineFromAgent = function (lineObj, time) {
    var msg = lineObj.text;
    msg = this.addEmoticons(msg);
    var html = '<div class="chatboxmessage"><span class="chatboxmessagefromAgent">'+ time + lineObj.by +':&nbsp;</span><span class="chatboxmessagecontentAgent">'+ msg +'</span></div>';
    if (typeof(this.lpc.getAgentName())=='undefined' || this.lpc.getAgentName()=='') {
        this.lpc.agent = lineObj.by;
        this.baseTitle = 'Agent ' + this.lpc.getAgentName();
        this.setTitle(this.baseTitle);
    }
    $("#chatboxcontent-"+this.randomNum).append(html);
    this.scrollToBottom();
};

chatGui.prototype.addEmoticons = function (msg) {
    msg = msg.replace(/\:\)\)/g,"<img src='img/laugh.png' border=0 width='13px' height='13px' />");
    msg = msg.replace(/\:\)/g,"<img src='img/smile.png' border=0 width='13px' height='13px' />");
    msg = msg.replace(/\:\(/g,"<img src='img/unhappy.png' border=0 width='13px' height='13px' />");
    return msg;
};

chatGui.prototype.myErrorHandler = function (errorobj){
    this.log('ERROR:' + errorobj.id);
    for (var p in errorobj) {
	    this.log(p + ' error: ' + errorobj[p]);
    }
    $("#chatboxcontent-"+this.randomNum).append('<div class="chatboxmessage"><span class="chatboxmessagefromError">'+ Error +':&nbsp;</span><span class="chatboxmessagecontentError">Error #'+ errorobj +' occured.</span></div>');
};

chatGui.prototype.setTitle = function (text) {
    $('#chatboxhead-'+this.randomNum).html(text);
};
chatGui.prototype.getTitle = function () {
    return $('#chatboxhead-'+this.randomNum).html();
};

chatGui.prototype.getCookie = function ( name ) {
    var start = document.cookie.indexOf( name + "=" );
    var len = start + name.length + 1;
    if ( ( !start ) && ( name != document.cookie.substring( 0, name.length ) ) ) {
        return null;
    }
    if ( start == -1 ) {
        return null;
    }
    var end = document.cookie.indexOf( ";", len );
    if ( end == -1 ) {
        end = document.cookie.length;
    }
    return unescape( document.cookie.substring( len, end ) );
};

chatGui.prototype.setCookie = function ( name, value, expires, path ) {
    // set time, it's in milliseconds
    var today = new Date();
    today.setTime( today.getTime() );
    
    if ( expires ) {
        expires = expires * 1000 * 60 * 60 * 24;
    }
    var expires_date = new Date( today.getTime() + (expires) );
    this.log('VALUE:'+value);
    document.cookie = name + "=" +escape( value ) +
        ( ( expires ) ? ";expires=" + expires_date.toGMTString() : "" ) +
        ( ( path ) ? ";path=" + path : "" );
};

chatGui.prototype.dir = function ( obj ) {
    //console.dir(obj);
}
chatGui.prototype.log = function ( msg ) {
    if(typeof(console)!='undefined') console.log(msg);
}

chatGui.prototype.deleteCookie = function ( name ) {
    if ( this.getCookie( name ) ) {
        document.cookie = name + "=" +
            ";expires=Thu, 01-Jan-1970 00:00:01 GMT";
    }
};