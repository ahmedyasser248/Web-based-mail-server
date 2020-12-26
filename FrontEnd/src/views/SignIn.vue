<template>
  <div class="signin">
    <v-img src="/space.jpeg">
      <h3 class="subheading grey--text ma-2">Sign in</h3>
      <v-card class="mx-auto my-12" max-width="374">
        <v-img height="250" src="/sign.jpg"></v-img>
        <v-card-text>
          <v-form class="px-3" ref="form">
            
            <v-text-field
              id="email"
              label="E-mail"
              v-model="email"
              autocomplete="off"
              prepend-icon="email"
              :rules="inputRules"
            ></v-text-field>

            <v-text-field
              id="password"
              v-model="pass"
              :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
              :rules="inputRules"
              :type="show1 ? 'text' : 'password'"
              name="input-10-1"
              autocomplete="off"
              label="Password"
              prepend-icon="lock"
              @click:append="show1 = !show1"
            ></v-text-field>
          </v-form>
        </v-card-text>
        <v-row class="ml-5">
          <v-col>
            <v-btn text router :to="'./signup'">Sign up</v-btn>
          </v-col>
          <v-spacer></v-spacer>
          <v-col>
            <v-btn
              id="sign-in"
              class="success"
              @click="signin"
              router
              :to="flag ? './inbox/' + email : './'"
              >Sign in</v-btn
            >
          </v-col>
        </v-row>
      </v-card>
    </v-img>
  </div>
</template>

<script>
import axios from "axios";
import { User } from "@/components/classes.js";
export default {
  name: "SignIn",
  data() {
    return {
      show1: false,
      flag: false,
      email: "",
      pass: "",
      inputRules: [
        (v) => !!v || "This field is required",
        //v => v.length >= 3 || 'Minimum length is 8 characters'
      ],
    };
  },
  
  methods: {
    async signin() {
      if (this.$refs.form.validate()) {
        await this.sendData();
        if (this.flag) {
          document.getElementById("sign-in").click();
        }
      }
    },
    async sendData() {
      let response = await axios.post(
        "http://localhost:8085/signIn",
        JSON.stringify(new User(this.email, this.pass, null, null)),
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data) {
        this.flag = true;
      } else {
        alert("Email or password is wrong");
      }
    },
  },
};
</script>

<style></style>>
