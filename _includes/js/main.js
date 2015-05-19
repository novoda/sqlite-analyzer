jQuery(document).ready(function($){
    // todo: jquery stuff should need to put here.
	
	$(tocList).empty();                                                            
    
    var prevH2Item = null;                                                            
    var prevH2List = null;                                                            
    
    var index = 0;                                                                    
    $(".page-content h2").each(function() {                                                     
    
        //insert an anchor to jump to, from the TOC link.            
        var anchor = "<a name='" + index + "'></a>";                 
        $(this).before(anchor);                                     
        
        var li     = "<li><a href='#" + index + "'>" + $(this).text() + "</a></li>"; 
        
        if( $(this).is("h2") ){                                     
            prevH2List = $("<ul></ul>");                
            prevH2Item = $(li);                                     
            prevH2Item.append(prevH2List);                          
            prevH2Item.appendTo("#tocList");                        
        } else {                                                    
            prevH2List.append(li);                                  
        }                                                           
        index++;                                                    
    });         
});
