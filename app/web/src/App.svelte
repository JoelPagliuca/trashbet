<script>
  import {
    Header,
    HeaderNav,
    HeaderNavItem,
    HeaderUtilities,
    SkipToContent,
    Content,
    Grid,
    Row,
    Column,
    HeaderGlobalAction,
  } from "carbon-components-svelte";
  import Logout20 from "carbon-icons-svelte/lib/Logout20";
  import BetList from "./components/BetList.svelte";
  import Login from "./components/Login.svelte";
  import Theme from "./components/Theme.svelte";
  import { store } from "./auth.js";

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
        <HeaderNavItem href="/" text="Home" />
      </HeaderNav>
      
      <HeaderUtilities>
        <HeaderGlobalAction aria-label="Logout" icon={Logout20} on:click={handleLogout}/>
      </HeaderUtilities>
    {/if}
  </Header>
    

  <Content>
    <Grid>
      <Row>
        <Column>
          {#if $store != null}
            <h2>Bets</h2>
            <BetList/>
          {:else}
            <Login/>
          {/if}
        </Column>
      </Row>
    </Grid>
  </Content>
</Theme>
