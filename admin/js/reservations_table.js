var Reservations = {


    filter: null,

    constructTable: function constructTable(){
        var filt = this.filter;
        var tbl = $('#myTable').DataTable({
            "columns":[{"mData": 0}, 
                       {"mData": 1,
                        "mRender":function(data,type,full){
                         return '<div style = "padding: 8px 0px 8px 0px; " class="row_data"  id = "airline">' + full[1] + '</div>';}   
                       }, 
                       {"mData": 2,
                        "mRender":function(data,type,full){
                         return '<div style = "padding: 8px 0px 8px 0px; " class="row_data"  id = "shipper_name">' + full[2] + '</div>';}   
                       }, 
                       {"mData": 3,
                       "mRender":function(data,type,full){
                        return '<div style = "padding: 8px 0px 8px 0px; " class="row_data"  id = "org_code">' + full[3] + '</div>';}   
                       }, 
                       {"mData": 4,
                       "mRender":function(data,type,full){
                        return '<div style = "padding: 8px 0px 8px 0px; " class="row_data"  id = "dest_code">' + full[4] + '</div>';}   
                       }, 
                       {"mData": 5,
                       "mRender":function(data,type,full){
                        return '<div style = "padding: 8px 0px 8px 0px; " class="row_data"  id = "ship_date">' + full[5] + '</div>';}   
                       }, 
                       {"mData": 6,
                       "mRender":function(data,type,full){
                        return '<div style = "padding: 8px 0px 8px 0px; " class="row_data"  id = "status">' + full[6] + '</div>';}   
                       }, 
                      
                       {"mData": "null",
                        "bSortable": false,
                        "mRender":function(data,type,full){
                            var btStat = "auto";
                            var accept = "btn-success";
                            var reject = "btn-danger";
                            if(full[6] != "Pending"){
                                btStat = "none";
                                accept = "btn-secondary";
                                reject = "btn-secondary";
                            }

                            var cont  = '<span style =" padding: 8px 0px 8px 0px;"   class="btn_info" >';
                                cont += '<button class="bt1 btn btn-primary btn-sm" title="More Details" id ="'+full[0]+ '">';
                                cont += '<i class="ic1 fa fa-fw fa-info"></i></button> </span>';
                                if(filt== "today"){
                                    cont += '<span style ="padding: 8px 0px 8px 0px;" class="btn_ship" >';
                                    cont += '<button class="bt3 btn  btn-sm btn-warning" title="Ship Reservation" id="'+full[0]+'">';
                                    cont += '<i class="ic2 fa fa-fw fa-paper-plane"></i></button> </span>';
                                }
                                if(filt == "shipped"){
                                    cont += '<span style ="padding: 8px 0px 8px 0px;" class="btn_message" >';
                                    cont += '<button class="bt3 btn  btn-sm btn-warning" title="Send Message" id="'+full[0]+'">';
                                    cont += '<i class="ic2 fa fa-fw fa-envelope"></i></button> </span>';
                                }
                                cont += '<span style =" padding: 8px 0px 8px 0px;pointer-events:' + btStat +'"  class="btn_accept" >';
                                cont += '<button class="bt2 btn   btn-sm '+ accept +'"title="Accept"  id="'+full[0]+'" >';
                                cont += '<i class="ic2 fa fa-fw fa-check"></i></button> </span>';
                                cont += '<span style ="padding: 8px 0px 8px 0px;pointer-events:' + btStat +'"  class="btn_reject" >';
                                cont += '<button class="bt3 btn  btn-sm '+ reject + '"title="Reject" id="'+full[0]+'">';
                                cont += '<i class="ic2 fa fa-fw fa-times"></i></button> </span>';
                               
                                return cont;
                        } 
                      }]
        });
    
        return tbl;
    
    },
    showData: function showData(filter){
    
        this.filter = filter;

        if(filter == "today")  title = "To be shipped today";
        else if(filter == "pending") title = "Pending Reservations";
        else if(filter == "all") title = "All Reservations";
        else if(filter == "accepted") title = "Accepted Reservations";
        else if(filter == "rejected") title = "Rejected Reservations";
        else if(filter == "shipped") title = "Shipped Reservations";
        else if(filter == "completed") title = "Completed Reservations";

        $('.table-card-header').html(title);
        $('#myTable').DataTable().destroy();
        $('#add_record').empty();
        $('#myTable').html("<thead> <tr> <td>ID</td> <td>Airline</td> <td>Shipper Name</td> <td>Origin</td> <td>Destination</td> <td>Shipping Date</td>"+
                        "<td>Status</td> <td> Command </td></tr> </thead> <tbody></tbody>");    
    
        var table = this.constructTable();
        var database = firebase.database();
        var ref = database.ref('Reservation');
    
        this.addTableListener(ref,table);
    },

    refreshTable: function refreshTable(table){
        var filt = this.filter;
        var database = firebase.database();
        var ref = database.ref('Reservation');
    
        ref.once('value',(snapshot) => {
        snapshot.forEach((snap) => {
        var reservation = snap.val();
        if(filt == "today"){ if(!isToday(reservation.shippingDate) || reservation.status != "Accepted")return;}
        if(filt == "accepted"){if(reservation.status != "Accepted")return;}
        if(filt == "rejected"){if(reservation.status != "Rejected")return;}
        if(filt == "pending"){if(reservation.status != "Pending")return;}
        if(filt == "shipped"){if(reservation.status != "Shipped") return;}
        if(filt == "completed"){if(reservation.status != "Arrived") return;}
        
     
        table.row.add([reservation.uid,
                       reservation.airline,
                       reservation.shipperName,
                       reservation.originCode,
                       reservation.destinationCode,
                       reservation.shippingDate,
                       reservation.status, 
                       ""]).draw()});});
        
    },

    addTableListener: function addTableListener(dbRef,table){

        var filt  = this.filter;
        //when new record is added to the DB, append it to the table.
        dbRef.on('child_added',(snapshot)=>{
            var reservation = snapshot.val();
         
            if(filt == "today"){ if(!isToday(reservation.shippingDate) || reservation.status != "Accepted")return;}
            if(filt == "accepted"){if(reservation.status != "Accepted")return;}
            if(filt == "rejected"){if(reservation.status != "Rejected")return;}
            if(filt == "pending"){if(reservation.status != "Pending")return;}
            if(filt == "shipped"){if(reservation.status != "Shipped") return;}
            if(filt == "completed"){if(reservation.status != "Arrived") return;}

             table.row.add([reservation.uid,
                reservation.airline,
                reservation.shipperName,
                reservation.originCode,
                reservation.destinationCode,
                reservation.shippingDate,
                reservation.status, 
                ""]).draw()
            });     
     
         //when a record was updated to the DB, refresh the table
         dbRef.on('child_changed',(snapshot)=>{
             table.clear().draw();
             this.refreshTable(table);
         });     
     
         //when a record was deleted to the DB, refresh the table
         dbRef.on('child_removed',(snapshot)=>{
             table.clear().draw();
             this.refreshTable(table);
         });
    },

    getReservationData: function getReservationData(id){


        var database = firebase.database();
        var ref = database.ref('Reservation');
        var reservation;
        ref.once('value',(snapshot) => {
            reservation =   snapshot.child(id).val();
        });

     
              
        //shipper's info
        $('#airline-logo').attr('src',reservation.imgUrl);
        $('#shippername-holder').html(reservation.shipperName);
        $('#address-holder').html(reservation.shipperAddress);
        ref = database.ref('Users/'+reservation.userId);
        ref.once('value',(snapshot) => {
            company = snapshot.child('company').val();
            phoneNumber = snapshot.child('phoneNumber').val();
            $('#company-holder').html(company);
            $('#phone-holder').html(phoneNumber);
        });
        


        

        //shipment's info
        $('#airline-holder').html(reservation.airline);
        $('#flightcode-holder').html(reservation.flightCode);
        $('#origin-holder').html(reservation.originName + " (" + reservation.originCode + ")");
        $('#destination-holder').html(reservation.destinationName + " (" + reservation.destinationCode + ")");
        $('#shipdate-holder').html(reservation.shippingDate);
        $('#status-holder').html(reservation.status);
        $('#weight-holder').html(reservation.weight);
        $('#dimension-holder').html(reservation.length + " x " + reservation.width + " x " + reservation.height);
        $('#pieces-holder').html(reservation.pieces);

        //consignee's info
        $('#coname-holder').html(reservation.consigneeName);
        $('#coaddress-holder').html(reservation.consigneeAddress);

    },

    reject: function reject(reservationId , message){
        //get the userId using the reservationID
        var database = firebase.database();
        var ref = database.ref('Reservation/'+reservationId+"/userId");
        var userId;
        ref.once('value',(snapshot)=>{
            userId = snapshot.val();
        });

        //set the status in the admin reservation table
        ref = database.ref('Reservation/'+reservationId+"/status");
        ref.set("Rejected");

        //set the status in the user's reservation table
        ref = database.ref("Users/"+userId+"/Reservation/"+ reservationId + "/status");
        ref.set("Rejected");

        //set the notif
        ref = database.ref('Users/'+userId+"/Notification");
        var flight = getFlight(reservationId);
        var notif= ref.push();
        ref.child(notif.key).set({
            uid:notif.key,
            reservationId:reservationId,
	        opened:false,
            status:"Rejected",
            flight:flight,
            message:message });

        

        $('#modalReject').modal('toggle');
        $('#reject_message').val('');
        
    },

    accept: function accept(reservationId, code , awb){
        //get the userId using the reservationID
        var database = firebase.database();
        var ref = database.ref('Reservation/'+reservationId+"/userId");
        var userId;
        ref.once('value',(snapshot)=>{
            userId = snapshot.val();
        });

        //set the status in the admin reservation table
        ref = database.ref('Reservation/'+reservationId+"/status");
        ref.set("Accepted");

        //set the flight code in the admin reservation table
        ref = database.ref('Reservation/'+reservationId+"/flightCode");
        ref.set(code);

        //set the status in the user's reservation table
        ref = database.ref("Users/"+userId+"/Reservation/"+ reservationId + "/status");
        ref.set("Accepted");

        //set the flight code in the user's reservation table
        ref = database.ref("Users/"+userId+"/Reservation/"+ reservationId + "/flightCode");
        ref.set(code);

        //set the notif
        ref = database.ref('Users/'+userId+"/Notification");
        var flight = getFlight(reservationId);
        var shipDate = getShipDate(reservationId);
        var notif= ref.push();
        
        

        var message = "Thank you so much for using our service!"
                    + " We had confirmed your reservation and it should be shipped on "
                    + shipDate
                    + ". Your tracking number is "
                    + code
                    + " , use our app inorder to track your cargo now."


        ref.child(notif.key).set({
            uid:notif.key,
            reservationId:reservationId,
            opened:false,
            status:"Accepted",
            flight:flight,
            awb:awb,
            message: message });


        $('#modalAccept').modal('toggle');
        $('#input_flight_code').val('');

    },

    shipCargo:  function sendCargo(reservationId , message){
     
          //get the userId using the reservationID
          var database = firebase.database();
          var ref = database.ref('Reservation/'+reservationId+"/userId");
          var userId;
          ref.once('value',(snapshot)=>{
              userId = snapshot.val();
          });

        //set the status in the admin reservation table
        ref = database.ref('Reservation/'+reservationId+"/status");
        ref.set("Shipped");

        //set the status in the user's reservation table
        ref = database.ref("Users/"+userId+"/Reservation/"+ reservationId + "/status");
        ref.set("Shipped");


        //set the notif
        ref = database.ref('Users/'+userId+"/Notification");
        var flight = getFlight(reservationId);

        var notif= ref.push();

        ref.child(notif.key).set({
            uid:notif.key,
            reservationId:reservationId,
 	        opened:false,
            status:"Shipped",
            flight:flight,
            message: message });

        $('#modalShip').modal('toggle');
        $('#shipping_message').val('');
    },

    sendMessage : function sendMessage(reservationId, message){
        
          //get the userId using the reservationID
          var database = firebase.database();
          var ref = database.ref('Reservation/'+reservationId+"/userId");
          var userId;
          ref.once('value',(snapshot)=>{
              userId = snapshot.val();
          });

        //set the status in the admin reservation table
        ref = database.ref('Reservation/'+reservationId+"/status");
        ref.set("Arrived");

        //set the status in the user's reservation table
        ref = database.ref("Users/"+userId+"/Reservation/"+ reservationId + "/status");
        ref.set("Arrived");


        //set the notif
        ref = database.ref('Users/'+userId+"/Notification");
        var flight = getFlight(reservationId);

        var notif= ref.push();

        ref.child(notif.key).set({
            uid:notif.key,
            reservationId:reservationId,
 	        opened:false,
            status:"Arrived",
            flight:flight,
            message: message });

        $('#modalSend').modal('toggle');
        $('#arrival_message').val('');
    }
       

}

function isToday(shipDate){
    let d =  moment(shipDate, 'DD / MM / YYYY').format("DD / MM / YYYY");
    let today =  moment(Date.now()).format('DD / MM / YYYY');
    
    if(d == today){return true;}
    return false;
}


function getFlight(reservationId){
     //get the origin and destination using the reservationID
     var database = firebase.database();
     var ref = database.ref('Reservation/'+reservationId);
     var flight;
    ref.once('value',(snapshot) =>{
        var reserv = snapshot.val();
        flight = reserv.originCode + "-" + reserv.destinationCode;
    });

    return flight;
}


function getShipDate(reservationId){
    //get the shipdate using the reservationID
    var database = firebase.database();
    var ref = database.ref('Reservation/'+reservationId+"/shippingDate");
    var date;
    ref.once('value',(snapshot) => {
        var sd = snapshot.val();
        date = moment(sd , 'DD / MM / YYYY').format('dddd, MMMM D');
    });
    return date;
}






