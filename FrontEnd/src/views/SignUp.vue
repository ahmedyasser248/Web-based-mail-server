<template>
  <div class="signup">
    <v-img src="/space.jpeg">
      <h3 class="subheading grey--text ma-2">Sign up</h3>
      <v-card class="mx-auto my-12" max-width="374">
        <v-img height="250" src="/sign.jpg"></v-img>
        <v-card-text>
          <v-form class="px-3" ref="form">
            <v-text-field
              label="First Name"
              v-model="firstname"
              prepend-icon="edit"
               autocomplete="off"
            ></v-text-field>
            <v-text-field
              label="Last Name"
              v-model="lastname"
              autocomplete="off"
              prepend-icon="edit"
              
            ></v-text-field>
            <v-text-field
              label="E-mail"
              v-model="email"
              autocomplete="off"
              prepend-icon="email"
              placeholder="example@mail.com"
            ></v-text-field>
            <v-text-field
              label="Password"
              v-model="pass"
              :type=" 'text' "
              autocomplete="off"
              prepend-icon="lock"
              hint="At least 8 characters"
              counter
              :rules="inputRules"
            ></v-text-field>
            <template>
              <v-row>
                <v-dialog
                  ref="dialog"
                  v-model="modal"
                  :return-value.sync="date"
                  width="290px"
                >
                  <template v-slot:activator="{ on, attrs }">
                    <v-text-field
                      v-model="date"
                      label="Picker in dialog"
                      prepend-icon="mdi-calendar"
                      readonly
                      v-bind="attrs"
                      v-on="on"
                    ></v-text-field>
                  </template>
                  <v-date-picker v-model="date" scrollable>
                    <v-spacer></v-spacer>
                    <v-btn text color="primary" @click="modal = false">
                      Cancel
                    </v-btn>
                    <v-btn
                      text
                      color="primary"
                      @click="$refs.dialog.save(date)"
                    >
                      OK
                    </v-btn>
                  </v-date-picker>
                </v-dialog>
              </v-row>
            </template>
          </v-form>
        </v-card-text>
        <v-row class="ml-5">
          <v-col>
            <v-btn id="sign-up" class="success" @click="signup"  :disabled="!formIsValid"
              router :to="flag ? './inbox/'+email: './signup'"
                >Sign up</v-btn>
          </v-col>
        </v-row>
      </v-card>
    </v-img>
  </div>
</template>

<script>
import {User} from '@/components/classes.js'
import axios from 'axios'
//import SignUpPop from '@/components/SignUpPop'
export default {
  name: "SignUp",
  //components: {SignUpPop},
  data() {
    return {
      date: new Date().toISOString().substr(0, 10),
      modal: false,
      flag: false,
      firstname: "",
      lastname: "",
      email: "",
      pass: "",
      inputRules: [
        (v) => !!v || "This field is required",
        (v) => v.length >= 8 || "Minimum length is 8 characters",
      ],
    };
  },
  computed: {
      formIsValid () {
        if(this.email != '' && this.pass != '' && this.firstname != '' && this.lastname != ''){
            return (true)
        }
        return(false)
      },
    },
  methods: {
   async signup(){
      if(this.flag==true){
          return
      }   
      var reg = /^([a-zA-Z0-9_\\.]+)@mail.com$/;
      var reg2 = /^([a-zA-Z]+)$/;
      var reg3 = /^([a-zA-Z0-9_\\.-]+)$/;
          if(!reg.test(this.email)){
            alert('Email address can only contain . _ characters and must be in the form @mail.com')
          }else if(!reg2.test(this.firstname)  || !reg2.test(this.lastname)){
            alert('First or Last names should only contain letters and numbers')
          }else if(!reg3.test(this.pass) || this.pass.length <8){
            alert('Password should be more than 8 Characters and contain letters and numbers and . or - or _  only')
          }
          else{
                      await this.sendData()
                      if(this.flag){
                        document.getElementById("sign-up").click()
                      }
            } } ,
      async  sendData(){
        let response = await axios.post('http://localhost:8085/SignUp',JSON.stringify(new User(this.email,this.pass,this.firstname,this.lastname)), {         
               headers:{
                 'Content-Type':'application/json'
               },
               
            })
            if(response.data){
              this.flag=true
               
            }
            else{
              alert("this email is already used")
            }

      }     
    }
  }

</script>

<style></style>>
