var Locations = {

    table : null,

    constructTable: function constructTable(){

        var tbl = $('#myTable').DataTable({
            "columns":[{"mData": 0}, 
                       {"mData": 1,
                        "mRender":function(data,type,full){
                         return '<div style = "padding: 8px 0px 8px 0px; " class="row_data editable"  id = "loc_name">' + full[1] + '</div>';}   
                       }, 
                       {"mData": 2,
                        "mRender":function(data,type,full){
                         return '<div style = "padding: 8px 0px 8px 0px; " class="row_data editable"  id = "loc_code">' + full[2] + '</div>';}   
                       }, 
                       {"mData": null,
                        "bSortable": false,
                        "mRender":function(data,type,full){
                            var cont  = '<span style =" padding: 8px 0px 8px 0px; margin-right:12px;"   class="btn_edit" >';
                                cont += '<button class="bt1 btn btn-primary btn-md " id ="'+full[0]+'" >';
                                cont += '<i class="ic1 fa fa-fw fa-edit"></i><span id="p1">Edit</span></button> </span>';
                                cont += '<span style =" padding: 8px 0px 8px 0px; margin-left:12px;"   class="btn_delete" >';
                                cont += '<button class="bt2 btn btn-danger btn-md "  id="'+full[0]+'" >';
                                cont += '<i class="ic2 fa fa-fw fa-trash"></i><span id="p2">Delete</span></button> </span></div>';
                                return cont;
                        } 
                      }]
        });
    
        return tbl;
    
    },
    showData: function showData(){
        
        $('.table-card-header').html('Locations Table');
        $('#myTable').DataTable().destroy();
        $('#myTable').html("<thead> <tr> <td>Id</td> <td>Location</td> <td>Code</td> <td>Command</td> </tr> </thead> <tbody></tbody>");    
        $('#add_record').html('<a href = ""  data-toggle="modal" data-target = "#modalAdd" title="Add new record" class="text-success" ><i class="fas fa-plus fa-2x"aria-hidden="true"></i></a>');
    
        this.table = this.constructTable();
        var database = firebase.database();
        var ref = database.ref('Locations');
    
        this.addTableListener(ref);
    },

    refreshTable: function refreshTable(){

        var database = firebase.database();
        var ref = database.ref('Locations');
    
        ref.once('value',(snapshot) => {
        snapshot.forEach((snap) => {
        var location = snap.val();
            
        this.table.row.add([location.uid , 
                       location.name,
                       location.code,""]).draw()});
    
       });
        
    },

    addTableListener: function addTableListener(dbRef){
        //when new record is added to the DB, append it to the table.
        dbRef.on('child_added',(snapshot)=>{
            var location = snapshot.val();
             this.table.row.add([location.uid , 
             location.name,
             location.code,
             ""]).draw()
            });     
     
    },

    saveData: function saveData(tbl_row){
        
        var uid = tbl_row.find('.bt1').attr('id');
        var database = firebase.database();
        var ref = database.ref('Locations/'+  uid);
        var name =  tbl_row.find('#loc_name').html();
        var code = tbl_row.find('#loc_code').html();
      
        ref.set({uid:uid,
                 name:name, 
                 code:code});

        this.table.clear().draw();
        this.refreshTable();

    },

    deleteData: function deleteData(id){
        var database = firebase.database();
        var ref = database.ref('Locations/'+  id);
        ref.remove();
        $('#modalDelete').modal('toggle');
        this.table.clear().draw();
        this.refreshTable();
    },

    addData: function addData(name,code){
        var database = firebase.database();
        var ref = database.ref('Locations');
        var id = ref.push();
        ref.child(id.key).set({
            uid:id.key,
            name:name,
            code:code, });
        $('#modalAdd').modal('toggle');
        $('#input_location').val('');
        $('#input_code').val('');
        this.table.clear().draw();
        this.refreshTable();
    }



}









