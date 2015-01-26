Thread.new do
  loop {
    puts Time.now;
    sleep 1
  }
end

run ->(env) {
  [200, {}, ["hello"]]
}