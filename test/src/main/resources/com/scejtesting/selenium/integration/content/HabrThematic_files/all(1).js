$(document).ready(function(){
	
	/** 
   * Нравится/не нравится хаб
   */
  $('#addHubMember').click(function(){
  	var link = $(this);
  			link.addClass('loading');
    var id = $(this).attr('data-id');
    $.post('/json/hubs/subscribe/', {'hub_id':id}, function(json){
      if(json.messages =='ok'){
        $('#removeHubMember').removeClass('hidden');
        $('#addHubMember').addClass('hidden'); 
        $('#members_count').text(json.members_count_str);       
      }else{
        show_system_error(json);
      }
      link.removeClass('loading');
    },'json');
    return false;
  });
  $('#removeHubMember').click(function(){
    var id = $(this).attr('data-id');
    var link = $(this);
  			link.addClass('loading');
  
      $.post('/json/hubs/unsubscribe/', {'hub_id':id}, function(json){
        if(json.messages =='ok'){
          $('#removeHubMember').addClass('hidden');
          $('#addHubMember').removeClass('hidden');        
          $('#members_count').text(json.members_count_str);
        }else{
          show_system_error(json);
        }
      	link.removeClass('loading');
      },'json');

    return false;
  });
  
})