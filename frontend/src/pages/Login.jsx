export default function Login() {
return ( <div className='min-h-screen flex items-center justify-center bg-gray-100'> <div className='bg-white p-8 rounded-lg shadow-md w-full max-w-md'> <h1 className='text-2xl font-bold mb-6 text-center'>
Login SkillBridge </h1>

```
    <div className='space-y-4'>
      <input
        type='email'
        placeholder='Email'
        className='w-full border p-3 rounded'
      />

      <input
        type='password'
        placeholder='Password'
        className='w-full border p-3 rounded'
      />

      <button className='w-full bg-blue-600 text-white p-3 rounded hover:bg-blue-700'>
        Masuk
      </button>
    </div>
  </div>
</div>

);
}
