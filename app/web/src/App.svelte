<script>
  import {
    Button,
    Header,
    HeaderNav,
    HeaderNavItem,
    HeaderNavMenu,
    HeaderUtilities,
    SkipToContent,
    Content,
    Grid,
    Row,
    Column,
    HeaderGlobalAction,
  } from "carbon-components-svelte";
  import UserAvatarFilledAlt from "carbon-icons-svelte/lib/UserAvatarFilledAlt20";
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
        <HeaderGlobalAction aria-label="Login" icon={UserAvatarFilledAlt}/>
      </HeaderUtilities>
    {/if}
  </Header>
    

  <Content>
    <Grid>
      <Row>
        <Column>
          {#if $store != null}
            <div>
              <Button Button on:click={handleLogout}>Logout</Button>
            </div>
          {:else}
            <Login/>
          {/if}
        </Column>
      </Row>
    </Grid>
  </Content>
</Theme>
