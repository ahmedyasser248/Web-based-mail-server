
<template>
  <div class="text-center">
    <v-dialog v-model="dialog" width="600">
      <template v-slot:activator="{ on, attrs }">
        <v-btn color="success" v-bind="attrs" v-on="on">
          Compose
          <v-icon right dark>
            mdi-plus
          </v-icon>
        </v-btn>
      </template>
      <v-card>
        <v-card-title class="grey lighten-2">
          Send an Email
           <v-spacer></v-spacer>
          <v-btn
                  color="error"
                  text
                  @click="close"
                >
                  <v-icon>close</v-icon>
                </v-btn>
        </v-card-title>

        <v-card-text>
          <v-form class="px-3" ref="form">
            <v-text-field
              label="To"
              v-model="To"
              prepend-icon="person"
              autocomplete="off"
              :rules="inputRules"
              hint="if you want to send for more than one type emails with , between them "
            ></v-text-field>

            <v-text-field
              label="Subject"
              v-model="Subject"
              prepend-icon="edit"
              autocomplete="off"
              :rules="inputRules"
            ></v-text-field>
            <v-textarea
              label="Message"
              v-model="content"
              prepend-icon="message"
              autocomplete="off"
              :rules="inputRules"
            ></v-textarea>
           <v-row>
            <v-col>
              <v-menu offset-y>
                <template v-slot:activator="{ on, attrs }">
                  <v-btn text color="grey" v-bind="attrs" v-on="on">
                    <span>Priority</span>
                    <v-icon right>expand_more</v-icon>
                  </v-btn>
                </template>
                <v-list dark>
                  <v-list-item @click="priority = 'Superhigh'">
                    <v-list-item-title>Superhigh</v-list-item-title>
                  </v-list-item>
                  <v-list-item @click="priority = 'high'">
                    <v-list-item-title>high</v-list-item-title>
                  </v-list-item>
                  <v-list-item @click="priority = 'medium'">
                    <v-list-item-title>medium</v-list-item-title>
                  </v-list-item>
                  <v-list-item @click="priority = 'low'">
                    <v-list-item-title>low</v-list-item-title>
                  </v-list-item>
                </v-list>
              </v-menu>
            </v-col>
            <v-col  >
              <h3 class="pa-3">
                 {{priority}}
              </h3>
            </v-col>
           </v-row>
           <v-row>
            <v-btn text class="success mx-0" @click="submit" >Send</v-btn>
            <v-spacer></v-spacer>
            <v-file-input id="choose" multiple show-size counter placeholder="Choose File"
              truncate-length="15" ref="myFileInput" 
            ></v-file-input >
            <v-spacer></v-spacer>
            <v-btn text class="error mx-0" @click="draft" >Draft</v-btn>
            </v-row>
          </v-form>
        </v-card-text>
      </v-card>
    </v-dialog>
  </div>
</template>
<script>
import {
  Mail,
  MessageBody,
  MailHeader,
  MailAttachements,
  MailID
} from "@/components/classes.js";
import axios from "axios";
export default {
  props: { username: String },
  data() {
    return {
      To: "",
      From: this.username,
      Subject: "",
      content: "",
      priority: "low",
    
      dialog: false,
      dialogDraft: false,
      date: new Date().toISOString(),
      inputRules: [
        (v) => !!v || "This field is required",
        (v) => v.length >= 1 || "Minimum length is 1 characters",
      ],
    };
  },
  methods: {
    close(){
       this.dialog=false
      this.clear()
      this.$refs.myFileInput.reset();
    },
    clear() {
      (this.To = ""),
        (this.Subject = ""),
        (this.content = ""),
        (this.priority = "low")
      
    },
    submit() {
      
      
      let array =this.To.split(',')
      if (this.$refs.form.validate()) {
      var bodyFormData = new FormData();
      bodyFormData.append("mail",JSON.stringify(new Mail(
                new MailAttachements(null),
                new MailHeader(
                  this.From,
                  array,
                  this.Subject,
                  this.date,
                  this.priority
                ),
                new MessageBody(this.content),
                new MailID(null,null,null))
              ))
      let g = document.getElementById("choose").files           
      for (let i=0;i<g.length;i++){
        bodyFormData.append("file",g[i])
      }
      var size=0
      for (let i=0;i<g.length;i++){
        size = size + g[i].size
      }
      if(size > 1001000000){
        alert("You exceeded the maximum size for the attachments which is 1giga ,Kindly Remove the attachments")
      }else{
      if(size > 20000000){
        alert("Uploading high size attachments will take a few moments >> Press OK to continue")
      }
        axios({
                  method: 'post',
                  url: 'http://localhost:8085/sendMail',
                  data: bodyFormData,
                  headers: {'Content-Type': 'multipart/form-data' }
                  })
          .then((response) => {
            if(response.data){
                this.clear();
                this.dialog = false;
                this.$refs.myFileInput.reset();
                g=null
                location.reload()
            }
            else{
              alert('one or all recievers are wrong')
            }
          });
      }  
      }
    },
    draft(){
      let array =this.To.split(',')
      let g = document.getElementById("choose").files
      if(this.To=='' && this.Subject=='' && this.content=='' && g.length==0){
         this.dialog = false;
         alert("all data are empty so nothing is saved")
         return
      }
        var bodyFormData = new FormData();
      bodyFormData.append("mail",JSON.stringify(new Mail(
                new MailAttachements(null),
                new MailHeader(
                  this.From,
                  array,
                  this.Subject,
                  this.date,
                  this.priority
                ),
                new MessageBody(this.content),
                new MailID(null,null,null))
              ))          
      for (let i=0;i<g.length;i++){
        bodyFormData.append("file",g[i])
      }
      var size=0
      for (let i=0;i<g.length;i++){
        size = size + g[i].size
      }
      if(size > 1001000000){
        alert("You exceeded the maximum size for the attachments which is 1giga ,Kindly Remove the attachments")
      }else{
      if(size > 20000000){
        alert("Uploading high size attachments will take a few moments >> Press OK to continue")
      }
      axios({
                  method: 'post',
                  url: 'http://localhost:8085/saveDraft',
                  data: bodyFormData,
                  headers: {'Content-Type': 'multipart/form-data' }
                  })

        this.clear();
                this.dialog = false;
                this.$refs.myFileInput.reset();
                location.reload()
  
      }  
    }
  },
};
</script>

<style></style>>
