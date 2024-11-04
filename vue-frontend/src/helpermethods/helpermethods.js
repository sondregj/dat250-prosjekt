export function createGuestUser() {
  fetch('http://localhost:8080/api/guest', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' }
  })
  .then(response => {
    if (response.status === 201) {
      return response.json(); // Return the promise here to chain the next .then()
    } else {
      return response.json().then(errorData => {
        console.log("Could not create user: ", errorData);
        throw new Error("Guest user creation failed");
      });
    }
  })
  .then(data => {
    console.log(data);
    localStorage.setItem("guest-id", data.guestId);
    console.log("Logged in as guest user with id: " + data.guestId);
  })
  .catch(error => {
    console.log("Error: " + error.message);
  });
}



export function createNewUser(username, password, email) {
  fetch('http://localhost:8080/api/users', {
    method: 'POST',
    body: JSON.stringify({
      username: username,
      password: password,
      email: email,
    }),
    headers: {
      'Content-Type': 'application/json',
    },
  })
    .then(response => {
      if (response.ok) {
        username = ''
        password = ''
        email = ''
        console.log('user created successfully')
      } else {
        return response.json().then(data => {
          throw new Error(data.message || 'Failed to create user')
        })
      }
    })
    .catch(error => {
      alert(error.message)
    })
}

export function loginUser(username, password) {
  fetch('http://localhost:8080/api/auth/login', {
    method: 'POST',
    body: JSON.stringify({
      username: username,
      password: password,
    }),
    headers: {
      'Content-Type': 'application/json',
      // TODO: response is returned as text for some reason
      Accept: 'application/json',
    },
  })
    .then(response => {
      if (response.ok) {
        username = ''
        password = ''
        console.log('user logged in successfully')
        return response.text()
      } else {
        return response.json().then(data => {
          throw new Error(data.message || 'Failed to login user')
        })
      }
    })
    .then(token => {
      localStorage.setItem("JWT", token);
      console.log('jwt:' + token);
    })
    .catch(error => {
      alert(error.message)
    })
}

export function createNewPoll(question, hoursvalid, voteoptions) {
  const now = new Date()
  const validUntil = new Date(now)
  const token = localStorage.getItem("JWT");
  if (!token){
    throw new Error("You need to be authorized to create a poll");
  }
  validUntil.setHours(now.getHours() + hoursvalid)
  fetch('http://localhost:8080/api/polls', {
    method: 'POST',
    body: JSON.stringify({
      question: question,
      publishedAt: now.getTime(),
      validUntil: validUntil.getTime(),
      voteOptions: voteoptions,
    }),
    headers: {
      'Content-Type': 'application/json',
      "Authorization": `Bearer ${token}`,
    },
  })
    .then(response => {
      if (response.status === 201) {
        console.log('poll created successfully')
      } else {
        return response.json().then(data => {
          console.log('Poll failed');
          throw new Error(data.message || 'Failed to create poll')
        })
      }
    })
    .catch(error => {
      alert(error.message)
    })
}

export async function getPoll(id) {
  try {
    const response = await fetch('http://localhost:8080/api/polls/'+id, {
      method: 'GET',
    })

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }
    //No content
    if (response.status == 204) {
      return []
    }

    return await response.json()
  } catch (error) {
    console.error('Failed to fetch polls', error)
    return []
  }
}


export async function getPolls() {
  try {
    const response = await fetch('http://localhost:8080/api/polls', {
      method: 'GET',
    })

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }
    //No content
    if (response.status == 204) {
      return []
    }

    return await response.json()
  } catch (error) {
    console.error('Failed to fetch polls', error)
    return []
  }
}

export async function addVote(voteoption) {
  const token = localStorage.getItem("JWT");
  if (!token){
    throw new Error("You need to be authorized to vote");
  }
  try {
    const response = await fetch('http://localhost:8080/api/votes', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify({
        publishedAt: Date.now(),
        voteOption: voteoption,
      }),
    })
    if (!response.ok) {
      throw new Error('Http error! Status: ' + response.status)
    }
    return await response.json()
  } catch (error) {
    console.error('Failed to vote', error)
    return null
  }
}

export async function addVoteGuest(voteoption) {
  try {
    const response = await fetch('http://localhost:8080/api/votes', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'GuestId': localStorage.getItem('guest-id'),
      },
      body: JSON.stringify({
        publishedAt: Date.now(),
        voteOption: voteoption,
      }),
    })
    if (!response.ok) {
      throw new Error('Http error! Status: ' + response.status)
    }
    return await response.json()
  } catch (error) {
    console.error('Failed to vote', error)
    return null
  }
}

export async function deletePoll(pollId) {
  const token = localStorage.getItem("JWT");
  if (!token){
    throw new Error("You need to be authorized to delete a poll");
  }
  try {
    const response = await fetch(`http://localhost:8080/api/polls/${pollId}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    })

    if (response.status === 404) {
      throw new Error(`Pole with ID ${pollId} not found`)
    }

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }
    console.log(`Poll with ID ${pollId} deleted successfully`, response)
    return response.json()
  } catch (error) {
    console.error(`Failed to delete poll with ID ${pollId}`, error)
    throw error
  }
}
