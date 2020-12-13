<script>
  import {
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
  import Theme from "./components/Theme.svelte";

  let isSideNavOpen = false;
</script>

<Theme theme="g90">
  <Header company="" platformName="Trashbet" bind:isSideNavOpen>
    <div slot="skip-to-content">
      <SkipToContent />
    </div>
    
    <HeaderNav>
      <HeaderNavItem href="/" text="Home" />
    </HeaderNav>
    
    <HeaderUtilities>
      <HeaderGlobalAction aria-label="Login" icon={UserAvatarFilledAlt}/>
    </HeaderUtilities>
  </Header>

  <Content>
    <Grid>
      <Row>
        <Column>
          <h1>Trashbet</h1>
        </Column>
      </Row>
    </Grid>
  </Content>
</Theme>
