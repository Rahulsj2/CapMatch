<template>
    <div id="app">
      <component :is = "layout">
        <router-view />
      </component>
      
    </div>
</template>

<script>

const defaultLayout = 'default'
export default {
  name: 'CapMatch',
  computed:{
    layout(){
      return (this.$route.meta.layout || defaultLayout) + '-layout'
    }
  }
}
</script>

<style lang="scss">
#app {
  font-family: 'Lato', sans-serif, Helvetica, Arial;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center; 
  color: #707070;
}


</style>
