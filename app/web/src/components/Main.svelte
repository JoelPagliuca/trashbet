<script>
  import { Column, Grid, Row } from "carbon-components-svelte";
  import { store } from "../auth";
  import BetList from "./BetList.svelte";
  import WagerList from "./WagerList.svelte";

  let wagers = []

  function updateWagers(event) {
    let newWager = event.detail
    wagers = [newWager, ...wagers]
    $store.amount -= newWager.amount
  }

</script>

<Grid>
  <Row>
    <Column>
      <h2>Bets</h2>
      <BetList on:wagerPlaced={updateWagers}/>
    </Column>
    <Column>
      <h2>Wagers</h2>
      <WagerList bind:wagers={wagers}/>
    </Column>
  </Row>
</Grid>
