<script>
  import { Button, Form, FormGroup, InlineLoading, TextInput } from "carbon-components-svelte";
  import { store } from "../auth.js";
  import { apiFetch } from "../api.js";
  let username = ""
  let password = ""
  let loginPromise = null

  async function login() {
    const res = await apiFetch("/login", {
      method: "POST",
      body: JSON.stringify({
        username: username,
        password: password,
      }),
    })
    if (res.ok) {
      $store = { "username": username }
      return
    } else {
      throw new Error("bad login")
    }
  }

  function handleSubmit() {
    loginPromise = login()
  }
</script>
<div>
  <h2>Login</h2>
  <Form on:submit="{handleSubmit}">
    <FormGroup>
      <TextInput labelText="Username" type="text" bind:value={username} />
      <TextInput labelText="Password" type="password" bind:value={password} />
    </FormGroup>
    <Button type="submit">Login</Button>
    {#await loginPromise}
      <InlineLoading status="active" description="loading..." />
    {:catch error}
      <InlineLoading status="error" description={error} />
    {/await}
  </Form>
</div>
