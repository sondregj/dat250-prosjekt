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


export async function createNewUser(username, password, email) {
  try {
    const response = await fetch('http://localhost:8080/api/users', {
      method: 'POST',
      body: JSON.stringify({
        username: username,
        password: password,
        email: email,
      }),
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (response.ok) {
      console.log('User created successfully');
      const data = await response.json();
      return data;
    } else {
      const data = await response.json();
      throw new Error(data.message || 'Failed to create user');
    }
  } catch (error) {
    console.error('Error creating user:', error.message);
    throw error;
  }
}

export async function loginUser(username, password) {
  try {
    const response = await fetch('http://localhost:8080/api/auth/login', {
      method: 'POST',
      body: JSON.stringify({
        username: username,
        password: password,
      }),
      headers: {
        'Content-Type': 'application/json',
        Accept: 'application/json',
      },
    });
    if (response.ok) {
      console.log('User logged in successfully');
      const data = await response.json();
      console.log(data)
      console.log('JWT:', data.token);
      return true; // Indicate successful login
    } else {
      const data = await response.json();
      throw new Error(data.message || 'Failed to login user');
    }
  } catch (error) {
    console.error('Error logging in:', error.message);
    return false; // Indicate login failure
  }
}

export function createNewPoll(question, hoursValid, voteOptions) {

  const now = new Date();
  const token = localStorage.getItem("JWT");
  let validUntil = new Date();

  console.log("Hours valid: ", hoursValid)


  if (!token){
    throw new Error("You need to be authorized to create a poll");
  }

  if(hoursValid > 0) {
     validUntil.setTime(validUntil.getTime() + hoursValid * 60 * 60 * 1000) //adds x amount of hours
  }

  console.log('Valid until after', validUntil)



  fetch('http://localhost:8080/api/polls', {
    method: 'POST',
    body: JSON.stringify({
      question: question,
      publishedAt: now.getTime(),
      validUntil: validUntil.getTime(),
      voteOptions: voteOptions,
    }),
    headers: {
      'Content-Type': 'application/json',
      "Authorization": `Bearer ${token}`,
    },
  })
    .then(response => {
      if (response.status === 201) {
          question = ''
        hoursValid = 0
        voteOptions = []
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
    if (!response.status === 201) {
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
    if (!response.status === 201) {
      throw new Error('Http error! Status: ' + response.status)
    }
    let data = await response.json()
    console.log(data)
    return data
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

export async function checkPollExpired(polls) {
  console.log('checking if poll is expired')

  for (const poll of polls) {
    const validUntil = poll.validUntil
    console.log('valid until before formatting: ', validUntil)
    const currentTime = Date.now()
    console.log('current time: ', currentTime)
    if (validUntil < currentTime) {

      const expiredPoll = {
        ...poll,
        validUntil: 0
      }

      poll.validUntil = 0 //marks the poll as expired locally
      await updatePoll(expiredPoll) //sets the poll object as expired in the database
      console.log(`Poll with id ${poll.id} is expired`)
    }
  }
}


export async function updatePoll(poll) {
  try {
    const pollId = poll.id;
    const response = await fetch(`http://localhost:8080/api/polls/${pollId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(poll)
    })

    if (!response.ok) {
      throw new Error('Failed to update poll')
    }

    return await response.json()
  } catch (error) {
    console.error('Error updating poll', error)
    throw error
  }
}

export async function checkGuest(guestid){
  try {
    const response = await fetch(`http://localhost:8080/api/guest/${guestid}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    })

    if (!response.ok) {
      throw new Error('Failed to check guest')
    }
    let data = await response.json()
    console.log(data)
    return data
  } catch (error) {
    console.error('Error checking guest user', error)
    throw error
  }
}

export async function getPollByUser(){
  try {
  let token = localStorage.getItem('JWT');
  if (!token){
    throw new Error("Has to be logged in")
  }
  let response = await fetch(`http://localhost:8080/api/polls/user`, {
    method: 'GET',
    headers: {
      "Content-Type": "application/json",
      "Authorization": `Bearer ${token}`,
    }
  })
  if (response.status === 204){
    console.log("No polls")
  } else if(!response.ok){
    throw new Error("Error fetching polls")
  }
    return response.json();
} catch(err){
  console.log(err)
}
}

export async function getVotesByUser(){
  try {
  let token = localStorage.getItem('JWT');
  if (!token){
    throw new Error("Has to be logged in")
  }
  let response = await fetch(`http://localhost:8080/api/votes`, {
    method: 'GET',
    headers: {
      "Content-Type": "application/json",
      "Authorization": `Bearer ${token}`,
    }
  })
  if (response.status === 204){
    console.log("No votes")
  } else if(!response.ok){
    throw new Error("Error fetching votes")
  }
    return response.json();
} catch(err){
  console.log(err)
}
}

export async function deleteVote(voteId) {
  const token = localStorage.getItem("JWT");
  if (!token){
    throw new Error("You need to be authorized to delete a vote");
  }
  try {
    const response = await fetch(`http://localhost:8080/api/votes/${voteId}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    })

    if (response.status === 404) {
      throw new Error(`Vote with ID ${voteId} not found`)
    }

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }
    console.log(`Vote with ID ${voteId} deleted successfully`, response)
    return response.json()
  } catch (error) {
    console.error(`Failed to delete poll with ID ${voteId}`, error)
    throw error
  }
}
