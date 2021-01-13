<script>
import { StructuredList, StructuredListBody, StructuredListCell, StructuredListHead, StructuredListRow, StructuredListSkeleton } from "carbon-components-svelte";
import { onMount } from "svelte";
import { apiFetch } from "../api";

  let bets = []

  onMount(async () => {
    const res = await apiFetch("/bet")
    bets = await res.json()
  })
</script>

{#if bets.length === 0}
  <StructuredListSkeleton rows={3}/>
{:else}
  <StructuredList border>
    <StructuredListHead>
      <StructuredListRow head>
        <StructuredListCell head>id</StructuredListCell>
        <StructuredListCell head>description</StructuredListCell>
        <StructuredListCell head>content</StructuredListCell>
      </StructuredListRow>
    </StructuredListHead>
    <StructuredListBody>
      {#each bets as bet}
      <StructuredListRow>
        <StructuredListCell>{bet.id}</StructuredListCell>
        <StructuredListCell>{bet.description}</StructuredListCell>
        <StructuredListCell>{JSON.stringify(bet)}</StructuredListCell>
      </StructuredListRow>
      {/each}
    </StructuredListBody>
  </StructuredList>
{/if}
