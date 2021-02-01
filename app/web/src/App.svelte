<script>
  import {
    Header,
    HeaderNav,
    HeaderUtilities,
    SkipToContent,
    Content,
    HeaderGlobalAction,
  } from "carbon-components-svelte";
  import Logout20 from "carbon-icons-svelte/lib/Logout20";
  import Login from "./components/Login.svelte";
  import Theme from "./components/Theme.svelte";
  import { store } from "./auth.js";
  import Main from "./components/Main.svelte";
  import Points from "./components/Points.svelte";

  let isSideNavOpen = false;

  function handleLogout() {
    $store = null
  }
</script>

<Theme theme="g90">
  
  <Header company="" platformName="Trashbet" bind:isSideNavOpen>
    <div slot="skip-to-content">
      <SkipToContent />
    </div>
    
    {#if $store != null}
      <HeaderNav>
        <Points/>
      </HeaderNav>
      
      <HeaderUtilities>
        <HeaderGlobalAction aria-label="Logout" icon={Logout20} on:click={handleLogout}/>
      </HeaderUtilities>
    {/if}
  </Header>
    
  <Content>
    {#if $store != null}
      <Main/>
    {:else}
      <Login/>
    {/if}
  </Content>
</Theme>
