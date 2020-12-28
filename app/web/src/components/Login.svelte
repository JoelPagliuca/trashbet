<script>
  import { Button, Form, FormGroup, TextInput } from "carbon-components-svelte";
  import { store } from "../auth.js";
  import { apiEndpoint } from "../environment.js";
  import { apiFetch } from "../api.js";
  let username = ""
  let password = ""
  function handleSubmit() {
    console.log("Login.handleSubmit()")
    apiFetch("/login", {
      method: "POST",
      body: JSON.stringify({
        username: username,
        password: password,
      }),
      credentials: "omit",
    })
  }
</script>
<div>
  <h2>Login</h2>
  <pre>{JSON.stringify($store, null, 2)}</pre>
  <Form on:submit="{handleSubmit}">
    <FormGroup>
      <TextInput labelText="Username" type="text" bind:value={username} />
      <TextInput labelText="Password" type="password" bind:value={password} />
    </FormGroup>
    <Button type="submit">Login</Button>
  </Form>
</div>
