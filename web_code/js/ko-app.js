var lineModel = function(id, title, link, text)
{
	this.art_id 	=	ko.observable(id);		// article ID; does NOT reflect order on screen
	this.art_title 	=	ko.observable(title);		// article ID; does NOT reflect order on screen
	this.art_link 	=	ko.observable(link);		// article ID; does NOT reflect order on screen
	this.art_text 	=	ko.observable(text);		// article ID; does NOT reflect order on screen
	
	this.orderIndex		=	0;									// order in the display list; will be set up at the re-indexing function
	
	this.showEditBtns	=	ko.observable(false);
	this.showDelBtn		=	ko.observable(true);
	
	this.categs		=	ko.observableArray([]);
}

var categoryModel	=	function(catName)
{
	this.cat_name	=	ko.observable(catName);
	console.log();
	
	this.cat_url	=	ko.observable( ('#' + encodeURIComponent(catName)).replace(/ /g, '+') );
	// console.log(catName);
}



// LIST: contains indexes of the lines that have been edited in current session
var	changedLinesList	=	[]

self	= null;
lastID	=	ko.observable(0);	/* this value is equivalent with not initialized */
topID	=	null;

var viewModel = function()
{
	
	// console.log( "cookie: " + (isCookieSet == "andrei") );
	// save the current instance reference of the ViewModel; "this" is local-consistent, not global-consistent
	self = this;

	// index of the last object added; start at "1"
	// self.index		= lines.length + 1;
	//self.index		=	0;
	self.act_index	=	ko.observable(0);
	
	self.cookieUser	=	ko.observable(readCookie("user"));
	self.login_user	=	ko.observable("andrei chelaru");
	self.login_pass	=	ko.observable("");
	
	
	self.logged_in	=	ko.observable( self.cookieUser() != null);
	
	// INIT: binding for the "lines" iterator
	self.lines		= 	ko.observableArray([]);
	self.categories	=	ko.observableArray([]);

	
	// HELPER:	re-index function
	self.reorderLines = function(lines)
	{
		for( tmp_index = 0; tmp_index < lines.length; tmp_index++ )
		{
			lines[tmp_index].orderIndex	=	tmp_index;	// added one, so indexes start from one
		}
	}
	
	
	// EVENT/CLICK: function to ADD a line
	self.addLine 	= function(line)
	{
		// console.log( this.orderIndex );
		
		// self.lines.push( new lineModel('super cool shit abcd urs polar ', self.index) );			//push at the end
		self.lines.splice( this.orderIndex, 0, new lineModel('super cool shit abcd urs polar ', self.index ) );	//push at the top
		
		// re-index the display order
		//for( tmp_index = 0; tmp_index < lines.length; tmp_index++ ){ lines[tmp_index].orderIndex	=	tmp_index; }
		
		self.reorderLines(lines);
		
		self.index++;
		
		// scroll-focus to newly created item; TODO
		// jQuery stuff, used for scrolling
		// $('body, html').finish();
		// $('body, html').animate({ scrollTop: $("#myID").offset().top }, 300);
	}
	
	// EVENT/CLICK: function to REMOVE the line
	self.removeLine = function(line)
	{
		if( self.lines().length > 1 )
		{
			self.lines.remove(line);
			self.reorderLines(lines);
		}
		else
			// console.log(line);
			this.showDelBtn(false);
	};

	// EVENT/CLICK: function to EDIT the lines
	self.editLine = function(line)
	{
		line.isEditable(true);
		// line.textStr('');
		
		line.showEditBtns(true);
		
		// line.isFocused(true);
		// console.log( line.isEditable() );
	};
	
	// EVENT/CLICK: function to restore default text upon clicking edit CANCEL button
	self.editCancel = function(line)
	{
		line.isEditable(false);
		line.textStr(line.defaultText);
		line.showEditBtns(false);
		
		// console.log(this.defaultText);
	}
	
	// EVENT/CLICK:	function to SYNCHRONIZE lines to back-server
	self.saveChanges		=	function(line)
	{
		csrftoken		=	$('input[name=csrfmiddlewaretoken]').val();
		
		line.isEditable(false);
		line.showEditBtns(false);

		this.defaultText=	line.textStr();
		
		var jsonData	= 'update=' + ko.toJSON(self.lines);
		jsonData		=	jsonData;
		// + '&csrfmiddlewaretoken=' + $('[name=csrfmiddlewaretoken]').val();

		$.post(
			'/someapp/ticket',
			jsonData,
			// this callback is executed if the post was successful
			function(returnedData)
			{
				// console.log(returnedData);
			}
		)
		
		// console.log(jsonData);
	};
	
	// EVENT/CLICK:	function to be hooked by non-implemented features
	self.dummyHook = function(line)
	{
		// console.log('dummyHook() called !!!');
	};
	
	
	// EVENT/KEYPRESS: function to handle ENTER KEY pressing on the input tab
	self.enterSearch = function(line,evt)
	{
		if(evt.keyCode === 13)
		{
			// line.textStr(line.textStr());	// force update of textStr observable
			self.saveChanges(line);
			// console.log(self.textStr());  
			// console.log(evt);  
		}
		return true;
	};
	
	
	// end View Model
}

// GET 'articles' JSON data
updateJsonDataArt = function()
{
	$.ajax({
	type: 'GET',
	dataType: "json",
	// dataType: 'text',
	url: "http://localhost:8080/nucsApp/articles",
	data:{start:lastID()},
	success: function(data){
		
		//console.log(self.lines + "huh");
		
		self.lines.removeAll();
		
		for( article in data.articles )
		{
			xx = function(req_id){
				//console.log(req_id);
				$.ajax({
					type:		"GET",
					dataType:	"json",
					url:		"http://localhost:8080/nucsApp/categories",
					data:		{art_id:req_id},
					success:	function(data){
						for( categ in data.categories )
						{
							self.lines[data.art_id].categs.push( new categoryModel(data.categories[categ].cat_name) );
							// console.log( data.categories[categ].cat_name);
						}
					}
				
				});
			}
			
			
			// console.log("param: " + data.articles[article].art_id);
			
			
			lineData = new lineModel(
					data.articles[article].art_id,
					data.articles[article].title,
					data.articles[article].link,
					data.articles[article].text
					);
			
			self.lines[data.articles[article].art_id] = lineData;
			
			self.lines.push( lineData );	
			
			lastID(data.current_page.art_id);
			
			/* first load of data */
			if(topID == null){
				topID =	lastID();
			}
			
			xx(data.articles[article].art_id);
			
		}
		
		//ko.applyBindings( xx );
		}
	});

}

// GET 'categories' JSON data
updateJsonDataCat = function()
{
	console.log("getting categories ...");
	$.ajax({
	type: 'GET',
	dataType: "json",
	// dataType: 'text',
	url: "http://localhost:8080/nucsApp/categories",
	data:{},
	success: function(data){
		
		// console.log(self.categories + "huh2");
		
		self.categories.removeAll();
		
		for( categ in data.categories )
		{
			self.categories.push(
				new categoryModel(
					data.categories[categ].cat_name
					)
				);
			
			// console.log(data.categories[categ].cat_name);
		}
		
		//ko.applyBindings( xx );
		}
	});

}

moveLeft = function()
{
	lastID( 1 + Number(lastID()) );	// careful ! returns a string and applies "+" operator over string
	updateJsonDataArt();
}

moveRight = function()
{
	lastID( lastID()-1 );	// order not important, "-" operator does not make sense on strings
	updateJsonDataArt();
}

doLogin = function()
{

	console.log("logging in ..." + self.login_user() + "   " + self.login_pass());
	
	$.ajax({
	type: 'GET',
	dataType: "json",
	// dataType: 'text',
	url: "http://localhost:8080/nucsApp/login",
	data:{"user":self.login_user(), "pass":self.login_pass()},
	success: function(data){
		logged	=	(data["logged_in"] === 'true');
		self.logged_in( logged );
		console.log( self.logged_in());
		}
	
	});

}

doLogout = function()
{

	console.log("logging out ..." );
	
	$.ajax({
	type: 'GET',
	dataType: "json",
	// dataType: 'text',
	url: "http://localhost:8080/nucsApp/logout",
	data:{},
	success: function(data){
		//logged	=	(data["logged_in"] === 'true');
		self.logged_in( false );
		self.act_index(0);
		console.log( self.logged_in());
		}
	
	});

}

setActive = function(index){
	self.act_index(index);
	console.log(index);
}

// -----------------------

// create instance of the viewModel; elements get added in reverse order
viewModelInstance = viewModel();

// apply the global bindings
ko.applyBindings( viewModelInstance );

updateJsonDataArt();
updateJsonDataCat();

console.log(window.location.hash);


// jQuery ajax setup for the csrftoken handling
function csrfSafeMethod(method) {
    // these HTTP methods do not require CSRF protection
    return (/^(GET|HEAD|OPTIONS|TRACE)$/.test(method));
}
$.ajaxSetup({
    beforeSend: function(xhr, settings) {
        if (!csrfSafeMethod(settings.type) && !this.crossDomain) {
            xhr.setRequestHeader("X-CSRFToken", csrftoken);
			xhr.setRequestHeader("Origin", "localhost");
        }
    }
});

function readCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1,c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
    }
    return null;
}