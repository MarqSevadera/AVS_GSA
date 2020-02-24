var Table = {
    makeRowEditable: function makeRowEditable(tbl_row){
        tbl_row.find('.editable')
        .attr('contenteditable', 'true')
        .addClass('bg-light')
        .css('color','black');
    
        tbl_row.find('.editable').each(function(index, val) 
        {  
            //this will help in case user decided to click on cancel button
            $(this).attr('original_entry', $(this).html());
        }); 	
        
        showSaveCancelButton(tbl_row);
    },

    disableRow: function disableRow(tbl_row){

        tbl_row.find('.editable').each(function(index, val) {   
            $(this).html( $(this).attr('original_entry') ); 
        }); 
        
        tbl_row.find('.editable')
        .attr('contenteditable', 'false')
        .removeClass('bg-light')
        .css('color','white');

        showEditDeleteButton(tbl_row);
    },

    
}





function showEditDeleteButton(tbl_row){

    //change class name from btn_save to btn_edit
    tbl_row.find('.btn_save').removeClass('btn_save').addClass('btn_edit');
    //change class name from btn_cancel to btn_delete
    tbl_row.find('.btn_cancel').removeClass('btn_cancel').addClass('btn_delete');

    tbl_row.find('.bt1').trigger('blur').removeClass('btn-success').addClass('btn-primary');
    tbl_row.find('.bt2').trigger('blur').removeClass('btn-secondary').addClass('btn-danger');
    tbl_row.find('.ic1').removeClass('fa-save').addClass('fa-edit');
    tbl_row.find('.ic2').removeClass('fa-times11').addClass('fa-trash');

    tbl_row.find('#p1').html('Edit');
    tbl_row.find('#p2').html('Delete');
}

function showSaveCancelButton(tbl_row){
    //change class name from btn_edit to btn_save
    tbl_row.find('.btn_edit').removeClass('btn_edit').addClass('btn_save');
    //change class name from btn_delete to btn_cancel
    tbl_row.find('.btn_delete').removeClass('btn_delete').addClass('btn_cancel');

    tbl_row.find('.bt1').trigger('blur').removeClass('btn-primary').addClass('btn-success');
    tbl_row.find('.bt2').removeClass('btn-danger').addClass('btn-secondary');
    tbl_row.find('.ic1').removeClass('fa-edit').addClass('fa-save');
    tbl_row.find('.ic2').removeClass('fa-trash').addClass('fa-times');

    tbl_row.find('#p1').html('Save');
    tbl_row.find('#p2').html('Cancel');
}



function getDate(){
    d = moment(Date.now()).format('MMMM DD, YYYY');
    $('#date-holder').html('<i class="fa fa-fw fa-calendar"></i>&nbsp ' + d);
}



function showReservation(){
     //set the title of the Table
     $('.card-header').html('All Reservations');
     //Destroy previous instance of DataTable
     $('#myTable').DataTable().destroy();
     //create the thead of the table
     $('#myTable').html("<thead><tr> <td>Id</td> <td>Location</td> <td>Code</td> <td>Command</td> </tr> </thead><tbody></tbody>");   

     table = $('#myTable').DataTable({
        "columns":[{"mData": 0} , {"mData": 1} , {"mData": 2} , 
                   {"mData": null,
                    "bSortable": false,
                    "mRender":function(){
                        return '<button class="btn_ edit btn btn-info btn-sm">Edit</button>';
                    } 
                  }]
    });


 
     var database = firebase.database();
     var ref = database.ref('Users');
     //when new record is added to the db, append it to the table.
     ref.on('child_added',(snapshot)=>{
     user = snapshot.val();
     table.row.add([user.uid , 
      user.username,
      user.email,
      user.company,
      user.address, ]).draw()
     });
 
     //when a record was updated to the db, refresh the table
     ref.on('child_changed',(snapshot)=>{
         table.clear().draw();
         refreshUserTable(table);
     });
 
     //when a record was deleted to the db, refresh the table
     ref.on('child_removed',(snapshot)=>{
         table.clear().draw();
         refreshUserTable(table);
     });
}


function checkSession(){
    var username = sessionStorage.getItem("username");
    if(username === null){
        window.location = "pagenotfound.html";
    }else{
        getDate();
        $('#admin_name').html(username);
    }
}


function changePass(){
    var oldpass = $('#oldpass').val();
    var newpass = $('#newpass').val();
    var confirmpass = $('#confirmpass').val();

    $('#oldpass').val('');
    $('#newpass').val('');
    $('#confirmpass').val('');

    var sessname = sessionStorage.getItem('username');
    var sesspass = sessionStorage.getItem("password");


    if(oldpass != sesspass || newpass != confirmpass){
        $('#modalChangePass').modal('toggle');
        $('#modalInvalid').modal();
    }else{
        $('#modalChangePass').modal('toggle');
        var database = firebase.database();
        var ref = database.ref('Admin/password');
        ref.set(newpass);
        $('#modalSuccess').modal();
        sessionStorage.clear();
        sessionStorage.setItem("username",sessname);
        sessionStorage.setItem("password",newpass);

    }
}
