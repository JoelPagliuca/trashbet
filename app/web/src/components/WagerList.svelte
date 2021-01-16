<script>
  import { StructuredList, StructuredListBody, StructuredListCell, StructuredListHead, StructuredListRow, StructuredListSkeleton } from "carbon-components-svelte";

  import { onMount } from "svelte";
  import { apiFetch } from "../api";

  export let wagers = []
  onMount(async () => {
    const res = await apiFetch("/wager/user")
    wagers = await res.json()
  })
</script>

{#if wagers.length === 0}
  <StructuredListSkeleton rows={3}/>
{:else}
  <StructuredList border>
    <StructuredListHead>
      <StructuredListRow head>
        <StructuredListCell head>Bet</StructuredListCell>
        <StructuredListCell head>Outcome</StructuredListCell>
        <StructuredListCell head>Amount</StructuredListCell>
      </StructuredListRow>
    </StructuredListHead>
    <StructuredListBody>
      {#each wagers as wager}
      <StructuredListRow label for="row-{wager.id}">
        <StructuredListCell>{wager.betId}</StructuredListCell>
        <StructuredListCell>{wager.outcome}</StructuredListCell>
        <StructuredListCell>{wager.amount}</StructuredListCell>
      </StructuredListRow>
      {/each}
    </StructuredListBody>
  </StructuredList>
{/if}
