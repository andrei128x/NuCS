var someLine = function(textStr, itemNumber)
{
	this.textStr		=	ko.observable(textStr);
	this.defaultText	=	textStr;							// default text to display upon Cancel
	
	this.itemNumber 	=	ko.observable(itemNumber);		// article ID; does NOT reflect order on screen
	this.orderIndex		=	0;									// order in the display list; will be set up at the re-indexing function
	
	this.isEditable		=	ko.observable(false);
	
	this.showEditBtns	=	ko.observable(false);
	this.showDelBtn		=	ko.observable(true);
}
	
// LIST: contains indexes of the lines that have been edited in current session
var	changedLinesList	=	[]
	
var viewModel = function(lines)
{
	// save the current instance reference of the ViewModel; "this" is local-consistent, not global-consistent
	var self = this;

	// index of the last object added; start at "1"
	self.index		= lines.length + 1;
	
	
	// INIT: binding for the "lines" iterator
	self.lines	= ko.observableArray(lines);

	
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
		console.log( this.orderIndex );
		
		// self.lines.push( new someLine('super cool shit abcd urs polar ', self.index) );			//push at the end
		self.lines.splice( this.orderIndex, 0, new someLine('super cool shit abcd urs polar ', self.index ) );	//push at the top
		
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
		
		console.log(this.defaultText);
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
				console.log(returnedData);
			}
		)
		
		console.log(jsonData);
	};
	
	// EVENT/CLICK:	function to be hooked by non-implemented features
	self.dummyHook = function(line)
	{
		console.log('dummyHook() called !!!');
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

// create instance of the viewModel; elements get added in reverse order
var viewModelInstance = viewModel([
		new someLine('hei rup hei rap 3', 3),
		new someLine('ala bala portocala 2', 2),
		new someLine('element 1', 1)
	]);

// apply the global bindings
ko.applyBindings( viewModelInstance );


// jQuery ajax setup for the csrftoken handling
function csrfSafeMethod(method) {
    // these HTTP methods do not require CSRF protection
    return (/^(GET|HEAD|OPTIONS|TRACE)$/.test(method));
}
$.ajaxSetup({
    beforeSend: function(xhr, settings) {
        if (!csrfSafeMethod(settings.type) && !this.crossDomain) {
            xhr.setRequestHeader("X-CSRFToken", csrftoken);
        }
    }
});
