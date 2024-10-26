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

export function createNewPoll(question, hoursvalid, voteoptions) {
  const now = new Date()
  const validUntil = new Date(now)
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
    },
  })
    .then(response => {
      if (response.ok) {
        question = ''
        hoursvalid = 0
        voteoptions = []
        console.log('poll created successfully')
      } else {
        return response.json().then(data => {
          throw new Error(data.message || 'Failed to create poll')
        })
      }
    })
    .catch(error => {
      alert(error.message)
    })
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

export async function getVoteOptions() {
  try {
    const response = await fetch('http://localhost:8080/api/voteoptions', {
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

export async function addVote(voteoption){
  try {
    const response = await fetch('http://localhost:8080/api/votes',{
      method: 'POST',
      headers: {'Content-Type':'application/json'},
      body: JSON.stringify({
        publishedAt: Date.now(),
        voteOption: voteoption
      })
    })
    if (!response.ok){
      throw new Error("Http error! Status: "+ response.status);
    }
    return await response.json();
  } catch(error){
    console.error('Failed to fetch polls', error)
    return null;
  }
}
