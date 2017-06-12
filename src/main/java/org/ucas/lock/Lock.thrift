namespace java org.ucas.lock

service LockService{
	bool acquire(1:string uuid)
	void release(1:string uuid)
}
