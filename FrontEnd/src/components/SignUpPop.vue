
<template>
  <div class="text-center">
    <v-dialog v-model="dialog" width="600">
      <template v-slot:activator="{ on, attrs }">
        <v-btn color="success" v-bind="attrs" v-on="on">
          Compose
          <v-icon right dark> mdi-plus </v-icon>
        </v-btn>
      </template>
      <v-card>
        <v-card-title class="grey lighten-2"> Send an Email </v-card-title>

        <v-card-text>
          <v-form class="px-3" ref="form">
            <v-text-field
              label="To"
              v-model="To"
              prepend-icon="person"
              :rules="inputRules"
            ></v-text-field>
            <v-text-field
              label="From"
              v-model="From"
              prepend-icon="person"
              :rules="inputRules"
            ></v-text-field>
            <v-text-field
              label="Subject"
              v-model="Subject"
              prepend-icon="edit"
              :rules="inputRules"
            ></v-text-field>
            <v-textarea
              label="Message"
              v-model="content"
              prepend-icon="message"
              :rules="inputRules"
            ></v-textarea>
            <v-btn text class="success mx-0" @click="submit">Send</v-btn>
          </v-form>
        </v-card-text>
      </v-card>
    </v-dialog>
  </div>
</template>


<v-btn id="sign-up" class="success" @click="signup"  :disabled="!formIsValid"
              router :to="flag ? this.link1 : this.link">Sign up</v-btn>

<script>
import SignUp from '@/views/SignUp.vue'
export default {
  data() {
      SignUp.data
  },
  methods: {},
};
</script>
