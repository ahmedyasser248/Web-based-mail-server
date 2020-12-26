<template>
  <v-app class="grey lighten-4">
    <v-img src="/space.jpeg">
    <v-content >
      <router-view></router-view>
    </v-content>
    </v-img>
  </v-app>
</template>

<script>


export default {
  name: 'App',
  data: () => ({
    
    //
  }),
};
</script>
