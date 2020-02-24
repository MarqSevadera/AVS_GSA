
var Users ={
    addTableListener: function addTableListener(dbRef,table){
                         //when new record is added to the db, append it to the table.
                         dbRef.on('child_added',(snapshot)=>{
                         var user = snapshot.val();
                         table.row.add([user.uid , 
                         user.username,
                         user.phoneNumber,
                         user.email,
                         user.company,
                         user.address]).draw()});
    
                        //when a record was updated to the db, refresh the table
                        dbRef.on('child_changed',(snapshot)=>{
                            table.clear().draw();
                            refreshUserTable(table);
                        });
                    
                        //when a record was deleted to the db, refresh the table
                        dbRef.on('child_removed',(snapshot)=>{
                            table.clear().draw();
                            refreshUserTable(table);
                        });
    
                    },

    showData: function showData(){
                    
                $('.table-card-header').html('Users Table');
                $('#myTable').DataTable().destroy();
                $('#add_record').empty();
                $('#myTable').html("<thead> <tr> <td>ID</td> <td>Username</td> <td>Contact No.</td> <td>Email</td> <td>Company</td> <td>Address</td> </tr> </thead> <tbody> </tbody>");
                
                var table = $('#myTable').DataTable();
                
                var database = firebase.database();
                var ref = database.ref('Users');
                
                this.addTableListener(ref,table);
            },


    refreshUserTable: function refreshTable(){
                  var database = firebase.database();
                  var ref = database.ref('Users');
                  ref.once('value',(snapshot) => {
                     snapshot.forEach((snap) => {
                      var user = snap.val();
                      table.row.add([user.uid , 
                                     user.username,
                                     user.phoneNumber,
                                     user.email,
                                     user.company,
                                     user.address]).draw()});
                 });   
            }
    
}

 /*content +='<tr row_id = "'+user.uid+'">';
          content += '<td><div style = "padding: 8px 0px 8px 0px; " class="row_data" col_name="uid">' + user.uid + '</div></td>';
          content += '<td><div style = "padding: 8px 0px 8px 0px; " class="row_data editable"  id = "username">' + user.username + '</div></td>';
          content += '<td><div style = "padding: 8px 0px 8px 0px; " class="row_data editable"  id = "email"   >' + user.email + '</div></td>';
          content += '<td><div style = "padding: 8px 0px 8px 0px; " class="row_data editable"  id = "company" >' + user.company + '</div></td>';
          content += '<td><div style = "padding: 8px 0px 8px 0px; " class="row_data editable"  id = "address" >' + user.address + '</div></td>';
          content += '<td><span style =" padding: 8px 0px 8px 0px;"   class="btn_edit" > <button  class="bt1 btn btn-primary btn-md " row_id="'+user.uid+'" ><i class="ic1 fa fa-fw fa-edit"></i></button> </span></td>';
          content += '<td><span style =" padding: 8px 0px 8px 0px;"   class="btn_delete" > <button  class="bt2 btn btn-danger btn-md " row_id="'+user.uid+'" > <i class="ic2 fa fa-fw fa-trash"></i></button> </span></td>';
          content += '</tr>';

          var button1 = '<span style =" padding: 8px 0px 8px 0px;"   class="btn_edit" > <button  class="bt1 btn btn-primary btn-md " row_id="'+user.uid+'" ><i class="ic1 fa fa-fw fa-edit"></i></button> </span></td>';*/


